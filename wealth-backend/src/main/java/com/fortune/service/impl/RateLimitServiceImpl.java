package com.fortune.service.impl;

import com.fortune.common.BizException;
import com.fortune.common.ResultCode;
import com.fortune.config.AuditProperties;
import com.fortune.service.RateLimitService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class RateLimitServiceImpl implements RateLimitService {

    private final AuditProperties auditProperties;
    private final Map<String, AtomicInteger> dailyCount = new ConcurrentHashMap<>();
    private final Map<String, AtomicInteger> burstCount = new ConcurrentHashMap<>();

    public RateLimitServiceImpl(AuditProperties auditProperties) {
        this.auditProperties = auditProperties;
    }

    @Override
    public void checkDailyCalculate(String openid) {
        String key = LocalDate.now() + ":daily:" + openid;
        int count = dailyCount.computeIfAbsent(key, k -> new AtomicInteger()).incrementAndGet();
        if (count > auditProperties.getDailyLimit()) {
            throw new BizException(ResultCode.TOO_MANY_REQUESTS, "今日测算次数已达上限，请明天再来");
        }
    }

    @Override
    public void checkBurst(String key) {
        String limitKey = LocalDate.now() + ":burst:" + key;
        int count = burstCount.computeIfAbsent(limitKey, k -> new AtomicInteger()).incrementAndGet();
        if (count > auditProperties.getBurstLimit()) {
            throw new BizException(ResultCode.TOO_MANY_REQUESTS, "请求过于频繁，请稍后再试");
        }
    }
}
