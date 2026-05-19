package com.fortune.dto;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "AI 连通性诊断响应")
public class AiDebugResponse {

    @Schema(description = "当前 provider", example = "deepseek")
    private String provider;

    @Schema(description = "当前 endpoint", example = "https://api.deepseek.com/chat/completions")
    private String endpoint;

    @Schema(description = "当前模型", example = "deepseek-v4-flash")
    private String model;

    @Schema(description = "是否调用成功", example = "true")
    private Boolean success;

    @Schema(description = "HTTP 状态码，异常时可能为空", example = "200")
    private Integer statusCode;

    @Schema(description = "诊断结果摘要", example = "DeepSeek 调用成功")
    private String message;

    @Schema(description = "AI 返回文本预览或错误响应体")
    private String detail;

    public String getProvider() {
        return provider;
    }

    public void setProvider(String provider) {
        this.provider = provider;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Boolean getSuccess() {
        return success;
    }

    public void setSuccess(Boolean success) {
        this.success = success;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }
}
