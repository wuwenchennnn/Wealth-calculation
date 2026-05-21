package com.fortune.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
@Schema(description = "AI人生画像报告请求")
public class CalculateRequest {

    @Schema(description = "微信/游客用户标识", example = "guest_xxx", required = true)
    @NotBlank(message = "游客标识不能为空")
    private String openid;

    @Schema(description = "所在城市", example = "上海", required = true)
    @NotBlank(message = "城市不能为空")
    private String city;

    @Schema(description = "年龄，范围 12-80", example = "28", required = true)
    @NotNull(message = "年龄不能为空")
    @Min(value = 12, message = "年龄不能小于12")
    @Max(value = 80, message = "年龄不能大于80")
    private Integer age;

    @Schema(description = "当前状态", example = "稳定上班", required = true)
    @NotBlank(message = "当前状态不能为空")
    private String currentStatus;

    @Schema(description = "每月可支配区间", example = "6000-10000", required = true)
    @NotBlank(message = "每月可支配区间不能为空")
    private String disposableRange;

    @Schema(description = "现有积蓄", example = "1万-5万", required = true)
    @NotBlank(message = "现有积蓄不能为空")
    private String existingSavings;

    @Schema(description = "生活节奏", example = "忙但还能掌控", required = true)
    @NotBlank(message = "生活节奏不能为空")
    private String lifeRhythm;

    @Schema(description = "消费习惯", example = "日常均衡型", required = true)
    @NotBlank(message = "消费习惯不能为空")
    private String spendingHabit;

    @Schema(description = "行动力自评", example = "计划很多但执行一般", required = true)
    @NotBlank(message = "行动力自评不能为空")
    private String actionStyle;
}
