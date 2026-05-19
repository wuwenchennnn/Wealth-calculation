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
            return mockReport(request) + "\n\nAI 服务调用失败，已自动回退到本地测算模板。失败原因：" + result.getMessage();
        }
        return mockReport(request) + "\n\n当前 AI provider 未识别，已自动回退到本地 mock 报告。";
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
            systemMessage.put("content", "你是一名专业的青年财富趣味测算师，输出内容要年轻化、真实、有共情，且仅作娱乐参考，禁止金融建议和封建迷信。");
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
        int winRate = Math.min(92, Math.max(18, request.getSalary().intValue() / 350 + request.getMoney().intValue() / 5000));
        int downPaymentAge = Math.min(55, request.getAge() + Math.max(3, 12 - request.getSalary().intValue() / 3000));
        int flatAge = Math.min(60, downPaymentAge + 8);
        int shineAge = Math.min(58, request.getAge() + 5);
        return """
                1. 【财富命格判定】
                你的财富命格更接近「潜力逆袭命」。现在的你不是一夜暴富型选手，而是靠稳定收入、清醒消费和持续积累慢慢翻盘的人。城市压力确实不小，但只要别被即时消费牵着走，你的财富曲线会比想象中更稳。

                2. 【同龄人段位评级】
                结合所在城市、年龄、收入和存款情况，你目前大约打败了同城 %d%% 的同龄人。这个水平不算躺赢，但也绝不是落后，属于「有压力但有操作空间」的阶段。

                3. 【人生关键财富时间轴】
                如果当前生活状态基本不变，你大概在 %d 岁左右摸到本地刚需房首付门槛，%d 岁前后进入相对松弛的生活阶段。%d 岁附近可能是你收入结构和职业状态明显改善的窗口期。

                4. 【一生财富走势预判】
                未来3年，你的关键任务是稳定现金流，少做冲动消费。未来5年，如果工作能力和收入增速跟上，存款会从「看着少」变成「真能扛事」。未来10年，你会明显感受到早期积累带来的安全感，但前提是别把每次涨薪都同步升级成消费。

                5. 【致命破财短板】
                你最大的破财点不是赚得不够，而是容易被小额高频消费偷走预算。奶茶、外卖、临时购物、情绪性消费单次都不疼，但月底合起来很扎心。第二个短板是对未来大额支出缺少提前规划，容易一边焦虑一边放任。

                6. 【AI专属逆袭改运方案】
                建议你先建立「固定储蓄优先」规则，工资到账当天先转出一笔安全垫，再安排消费。日常消费保持一个可承受档位，减少无感支出；职业上优先提升能直接提高议价能力的技能，别轻易相信所谓稳赚捷径。你的逆袭不靠玄学，靠的是每个月都比上个月更清醒一点。

                本测算仅为趣味娱乐，人生财富终由自己努力决定
                """.formatted(winRate, downPaymentAge, flatAge, shineAge);
    }
}
