package com.fortune.service.impl;

import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.fortune.config.AiProperties;
import com.fortune.dto.AiDebugResponse;
import com.fortune.dto.CalculateRequest;
import com.fortune.service.AiService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class AiServiceImpl implements AiService {

    private final AiProperties aiProperties;
    private final RestTemplate restTemplate;

    public AiServiceImpl(AiProperties aiProperties) {
        this.aiProperties = aiProperties;
        this.restTemplate = buildRestTemplate(aiProperties);
    }

    @Override
    public String generateReport(CalculateRequest request, String prompt) {
        if (StrUtil.equalsIgnoreCase(aiProperties.getProvider(), "mock")) {
            return mockReport(request);
        }
        if (StrUtil.equalsAnyIgnoreCase(aiProperties.getProvider(), "deepseek", "openai-compatible")) {
            AiDebugResponse result = requestDeepSeek(prompt);
            if (Boolean.TRUE.equals(result.getSuccess())) {
                return result.getDetail();
            }
            log.warn("DeepSeek report generation failed. statusCode={}, message={}, detail={}",
                    result.getStatusCode(), result.getMessage(), result.getDetail());
            return mockReport(request) + "\n\nAI 服务调用失败，已自动回退到本地画像报告模板。失败原因：" + result.getMessage();
        }
        return mockReport(request) + "\n\n当前 AI provider 未识别，已自动回退到本地画像报告。";
    }

    @Override
    public AiDebugResponse debugDeepSeek() {
        return requestDeepSeek("请只回复一句话：DeepSeek 连通性测试成功。不要输出其他内容。");
    }

    private AiDebugResponse requestDeepSeek(String prompt) {
        String endpoint = aiProperties.getEndpoint();
        String model = StrUtil.blankToDefault(aiProperties.getModel(), "deepseek-v4-flash");

        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            List<MediaType> mediaTypes = new ArrayList<>();
            mediaTypes.add(MediaType.APPLICATION_JSON);
            headers.setAccept(mediaTypes);
            if (StrUtil.isNotBlank(aiProperties.getApiKey())) {
                headers.setBearerAuth(aiProperties.getApiKey());
            }

            JSONObject payload = new JSONObject();
            payload.put("model", model);
            payload.put("temperature", 0.8);
            payload.put("max_tokens", 1800);
            payload.put("stream", false);

            JSONArray messages = new JSONArray();
            JSONObject systemMessage = new JSONObject();
            systemMessage.put("role", "system");
            systemMessage.put("content", "你是一名专业的 AI 趣味人生画像助手，输出内容要年轻化、有趣、有共情，且仅供休闲娱乐与自我观察，禁止专业诊断、金融建议、封建迷信和收益承诺。");
            messages.add(systemMessage);

            JSONObject userMessage = new JSONObject();
            userMessage.put("role", "user");
            userMessage.put("content", prompt);
            messages.add(userMessage);
            payload.put("messages", messages);

            HttpEntity<String> entity = new HttpEntity<>(payload.toJSONString(), headers);
            ResponseEntity<String> response = restTemplate.exchange(endpoint, HttpMethod.POST, entity, String.class);
            String body = response.getBody();
            int statusCode = response.getStatusCodeValue();

            if (!response.getStatusCode().is2xxSuccessful()) {
                return buildDebugResponse(false, statusCode, "DeepSeek HTTP 状态码异常", safePreview(body));
            }
            if (StrUtil.isBlank(body)) {
                return buildDebugResponse(false, statusCode, "DeepSeek 返回体为空", null);
            }

            JSONObject root = JSON.parseObject(body);
            JSONObject error = root.getJSONObject("error");
            if (error != null) {
                return buildDebugResponse(false, statusCode, "DeepSeek 返回 error", safePreview(error.toJSONString()));
            }

            JSONArray choices = root.getJSONArray("choices");
            if (choices == null || choices.isEmpty()) {
                return buildDebugResponse(false, statusCode, "DeepSeek 返回 choices 为空", safePreview(body));
            }

            JSONObject firstChoice = choices.getJSONObject(0);
            JSONObject message = firstChoice.getJSONObject("message");
            String content = message == null ? null : message.getString("content");
            if (StrUtil.isBlank(content)) {
                return buildDebugResponse(false, statusCode, "DeepSeek 返回 content 为空", safePreview(body));
            }
            return buildDebugResponse(true, statusCode, "DeepSeek 调用成功", content.trim());
        } catch (HttpStatusCodeException ex) {
            String responseBody = ex.getResponseBodyAsString();
            log.warn("DeepSeek HTTP error. statusCode={}, body={}", ex.getRawStatusCode(), responseBody, ex);
            return buildDebugResponse(false, ex.getRawStatusCode(), "DeepSeek HTTP 错误：" + ex.getStatusText(), safePreview(responseBody));
        } catch (ResourceAccessException ex) {
            log.warn("DeepSeek network or timeout error. endpoint={}, model={}", endpoint, model, ex);
            return buildDebugResponse(false, null, "DeepSeek 网络或超时错误：" + ex.getMessage(), null);
        } catch (Exception ex) {
            log.warn("DeepSeek unexpected error. endpoint={}, model={}", endpoint, model, ex);
            return buildDebugResponse(false, null, "DeepSeek 未知错误：" + ex.getClass().getSimpleName() + " - " + ex.getMessage(), null);
        }
    }

    private AiDebugResponse buildDebugResponse(Boolean success, Integer statusCode, String message, String detail) {
        AiDebugResponse response = new AiDebugResponse();
        response.setProvider(aiProperties.getProvider());
        response.setEndpoint(aiProperties.getEndpoint());
        response.setModel(StrUtil.blankToDefault(aiProperties.getModel(), "deepseek-v4-flash"));
        response.setSuccess(success);
        response.setStatusCode(statusCode);
        response.setMessage(message);
        response.setDetail(detail);
        return response;
    }

    private String safePreview(String text) {
        if (StrUtil.isBlank(text)) {
            return text;
        }
        return text.length() > 2000 ? text.substring(0, 2000) : text;
    }

    private RestTemplate buildRestTemplate(AiProperties properties) {
        SimpleClientHttpRequestFactory factory = new SimpleClientHttpRequestFactory();
        factory.setConnectTimeout(properties.getConnectTimeoutMs() == null ? 5000 : properties.getConnectTimeoutMs());
        factory.setReadTimeout(properties.getReadTimeoutMs() == null ? 30000 : properties.getReadTimeoutMs());
        return new RestTemplate(factory);
    }

    private String mockReport(CalculateRequest request) {
        String city = request.getCity();
        String status = request.getCurrentStatus();
        String savings = request.getExistingSavings();
        return """
                # 你的《AI人生画像报告》
                📍 %s · %d岁 · %s | %s · 行动力观察中

                1. 【成长类型画像】—— 慢热潜力型
                你的画像更接近「慢热潜力型」。你不是靠一时冲劲持续推进的人，更适合在稳定节奏里慢慢建立掌控感。只要把目标拆小、减少被日常事务打断的次数，你会更容易进入持续成长的状态。

                2. 【当前阶段状态】—— 调整蓄力期
                你现在更像处在「调整蓄力期」：对未来有期待，也愿意做改变，但行动节奏偶尔会被压力、作息或琐事打散。这个阶段不用急着证明什么，先把生活节奏和小目标重新排顺，会更容易恢复能量。

                3. 【城市生活节奏参考】
                %s 的生活节奏会影响你的时间分配和恢复速度。城市机会多时，信息和选择也会更多；节奏偏快或通勤压力偏高时，人更容易把精力消耗在日常应对上。建议你把城市环境当作背景参考，而不是给自己贴固定标签。

                4. 【你的潜力优势】
                从你的选择看，你对自身状态是有感知的，尤其在“%s”这一项上，说明你并不是完全随波逐流的人。只要把注意力从泛泛焦虑转向具体行动，你的优势会体现在持续学习、适应变化和慢慢建立个人节奏上。

                5. 【容易拖慢你的习惯】
                容易拖慢你的，可能不是能力不够，而是目标太散、恢复不足或执行断档。生活节奏一乱，人就容易用刷手机、临时消费或拖延来缓解压力。真正需要调整的是每天能稳定重复的小动作，而不是一次性做很大的改变。

                6. 【7天轻量行动建议】
                - 第1-2天：记录每天最消耗精力的3件小事，先找到状态被打断的位置。
                - 第3-7天：根据你当前“%s”的状态，设定一个每天20分钟的小任务，完成比完美更重要。给自己留出固定休息时间，让行动和恢复形成循环。

                本报告仅供休闲娱乐与自我观察，不构成任何专业建议
                """.formatted(city, request.getAge(), status, request.getLifeRhythm(), city, savings, savings);
    }
}
