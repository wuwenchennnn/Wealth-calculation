package com.fortune.controller;

import com.fortune.common.ApiResponse;
import com.fortune.dto.AiDebugResponse;
import com.fortune.dto.CalculateRequest;
import com.fortune.dto.CalculateResponse;
import com.fortune.dto.UnlockRequest;
import com.fortune.service.AiService;
import com.fortune.service.CalculateService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@Tag(name = "AI人生画像", description = "游客趣味测评、报告解锁和 AI 连通性诊断")
@Validated
@RestController
@RequestMapping("/api/calculate")
public class CalculateController {

    private final CalculateService calculateService;
    private final AiService aiService;

    public CalculateController(CalculateService calculateService, AiService aiService) {
        this.calculateService = calculateService;
        this.aiService = aiService;
    }

    @Operation(summary = "提交游客趣味测评", description = "无需微信登录，根据城市、生活节奏和行为习惯生成 AI 人生画像报告")
    @PostMapping
    public ApiResponse<CalculateResponse> calculate(@RequestBody @Valid CalculateRequest request) {
        return ApiResponse.success(calculateService.calculate(request));
    }

    @Operation(summary = "诊断 DeepSeek 连通性", description = "通过 Swagger 直接测试 DeepSeek 配置、网络、鉴权、模型和响应解析是否正常")
    @GetMapping("/ai/debug")
    public ApiResponse<AiDebugResponse> debugAi() {
        return ApiResponse.success(aiService.debugDeepSeek());
    }

    @Operation(summary = "解锁当前完整报告", description = "根据当前测算记录 ID 和游客标识解锁完整测算报告")
    @PostMapping("/unlock")
    public ApiResponse<CalculateResponse> unlock(@RequestBody @Valid UnlockRequest request) {
        return ApiResponse.success(calculateService.unlock(request.getRecordId(), request.getOpenid()));
    }
}
