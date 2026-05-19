package com.fortune.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "报告解锁请求")
public class UnlockRequest {

    @Schema(description = "测算记录 ID", example = "1", required = true)
    @NotNull(message = "记录ID不能为空")
    private Long recordId;

    @Schema(description = "游客标识", example = "mock-openid", required = true)
    @NotBlank(message = "游客标识不能为空")
    private String openid;

    @Schema(description = "广告观看回执", example = "mock-ad-receipt", required = true)
    @NotBlank(message = "广告回执不能为空")
    private String adReceipt;
}
