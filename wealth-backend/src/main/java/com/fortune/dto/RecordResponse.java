package com.fortune.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "测算记录响应")
public class RecordResponse {
    @Schema(description = "记录 ID", example = "1")
    private Long id;

    @Schema(description = "城市", example = "上海")
    private String city;

    @Schema(description = "年龄", example = "30")
    private Integer age;

    @Schema(description = "月收入")
    private BigDecimal monthlyIncome;

    @Schema(description = "存款")
    private BigDecimal savings;

    @Schema(description = "消费档位", example = "medium")
    private String consumeLevel;

    @Schema(description = "工作状态", example = "employee")
    private String workStatus;

    @Schema(description = "摘要报告")
    private String summaryReport;

    @Schema(description = "完整报告")
    private String fullReport;

    @Schema(description = "是否已解锁", example = "false")
    private Boolean unlocked;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
