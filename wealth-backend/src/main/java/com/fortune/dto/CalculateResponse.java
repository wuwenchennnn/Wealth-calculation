package com.fortune.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@Schema(description = "AI人生画像报告响应")
public class CalculateResponse {
    @Schema(description = "画像记录 ID", example = "1")
    private Long recordId;

    @Schema(description = "城市", example = "上海")
    private String city;

    @Schema(description = "摘要报告")
    private String summaryReport;

    @Schema(description = "完整报告，未解锁时可能为空")
    private String fullReport;

    @Schema(description = "是否已解锁完整报告", example = "false")
    private Boolean unlocked;

    @Schema(description = "娱乐性质提示")
    private String entertainmentNotice;
}
