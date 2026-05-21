package com.fortune.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
@Schema(description = "AI人生画像记录响应")
public class RecordResponse {
    @Schema(description = "记录 ID", example = "1")
    private Long id;

    @Schema(description = "城市", example = "上海")
    private String city;

    @Schema(description = "年龄", example = "28")
    private Integer age;

    @Schema(description = "当前状态", example = "稳定上班")
    private String currentStatus;

    @Schema(description = "每月可支配区间", example = "6000-10000")
    private String disposableRange;

    @Schema(description = "现有积蓄", example = "1万-5万")
    private String existingSavings;

    @Schema(description = "生活节奏", example = "忙但还能掌控")
    private String lifeRhythm;

    @Schema(description = "消费习惯", example = "日常均衡型")
    private String spendingHabit;

    @Schema(description = "行动力自评", example = "计划很多但执行一般")
    private String actionStyle;

    @Schema(description = "摘要报告")
    private String summaryReport;

    @Schema(description = "完整报告")
    private String fullReport;

    @Schema(description = "是否已解锁", example = "false")
    private Boolean unlocked;

    @Schema(description = "创建时间")
    private LocalDateTime createTime;
}
