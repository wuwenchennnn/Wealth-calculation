package com.fortune.service.impl;

import com.fortune.common.BizException;
import com.fortune.common.ResultCode;
import com.fortune.dto.CalculateRequest;
import com.fortune.dto.CalculateResponse;
import com.fortune.entity.UserCalculateRecord;
import com.fortune.mapper.UserCalculateRecordMapper;
import com.fortune.service.AiService;
import com.fortune.service.CalculateService;
import com.fortune.service.CityCostService;
import com.fortune.service.RateLimitService;
import com.fortune.util.CacheKeyUtil;
import com.fortune.util.PromptUtil;
import com.fortune.util.ReportUtil;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CalculateServiceImpl implements CalculateService {

    private final UserCalculateRecordMapper recordMapper;
    private final CityCostService cityCostService;
    private final RateLimitService rateLimitService;
    private final AiService aiService;
    private final Map<String, CalculateResponse> aiCache = new ConcurrentHashMap<>();

    public CalculateServiceImpl(UserCalculateRecordMapper recordMapper,
                                CityCostService cityCostService,
                                RateLimitService rateLimitService,
                                AiService aiService) {
        this.recordMapper = recordMapper;
        this.cityCostService = cityCostService;
        this.rateLimitService = rateLimitService;
        this.aiService = aiService;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CalculateResponse calculate(CalculateRequest request) {
        rateLimitService.checkBurst(request.getOpenid());
        rateLimitService.checkDailyCalculate(request.getOpenid());
        cityCostService.getByCity(request.getCity());

        String cacheKey = CacheKeyUtil.calculateKey(request);
        CalculateResponse cached = aiCache.get(cacheKey);
        String fullReport;
        String summaryReport;
        if (cached != null) {
            fullReport = cached.getFullReport();
            summaryReport = cached.getSummaryReport();
        } else {
            String prompt = PromptUtil.build(request);
            fullReport = ReportUtil.sanitize(aiService.generateReport(request, prompt));
            summaryReport = ReportUtil.summary(fullReport);
            aiCache.put(cacheKey, CalculateResponse.builder()
                    .city(request.getCity())
                    .summaryReport(summaryReport)
                    .fullReport(fullReport)
                    .unlocked(false)
                    .entertainmentNotice("本测算仅为趣味娱乐参考")
                    .build());
        }

        UserCalculateRecord record = new UserCalculateRecord();
        record.setOpenid(request.getOpenid());
        record.setCity(request.getCity());
        record.setAge(request.getAge());
        record.setMonthlyIncome(request.getSalary());
        record.setSavings(request.getMoney());
        record.setConsumeLevel(request.getConsume());
        record.setWorkStatus(request.getWorkType());
        record.setSummaryReport(summaryReport);
        record.setFullReport(fullReport);
        record.setUnlocked(0);
        record.setCreateTime(LocalDateTime.now());
        record.setUpdateTime(LocalDateTime.now());
        recordMapper.insert(record);

        return CalculateResponse.builder()
                .recordId(record.getId())
                .city(record.getCity())
                .summaryReport(summaryReport)
                .fullReport(null)
                .unlocked(false)
                .entertainmentNotice("本测算仅为趣味娱乐参考，不构成任何金融或理财建议")
                .build();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public CalculateResponse unlock(Long recordId, String openid) {
        UserCalculateRecord record = recordMapper.selectById(recordId);
        if (record == null || !record.getOpenid().equals(openid)) {
            throw new BizException(ResultCode.NOT_FOUND, "测算记录不存在");
        }
        record.setUnlocked(1);
        record.setUpdateTime(LocalDateTime.now());
        recordMapper.updateById(record);
        return CalculateResponse.builder()
                .recordId(record.getId())
                .city(record.getCity())
                .summaryReport(record.getSummaryReport())
                .fullReport(record.getFullReport())
                .unlocked(true)
                .entertainmentNotice("本测算仅为趣味娱乐参考，不构成任何金融或理财建议")
                .build();
    }
}
