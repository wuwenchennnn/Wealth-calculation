package com.fortune.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Data
@Schema(description = "财富测算请求")
public class CalculateRequest {

    @Schema(description = "微信/游客用户标识", example = "mock-openid", required = true)
    @NotBlank(message = "游客标识不能为空")
    private String openid;

    @Schema(description = "所在城市", example = "上海", required = true)
    @NotBlank(message = "城市不能为空")
    private String city;

    @Schema(description = "年龄，范围 18-60", example = "30", required = true)
    @NotNull(message = "年龄不能为空")
    @Min(value = 18, message = "年龄不能小于18")
    @Max(value = 60, message = "年龄不能大于60")
    private Integer age;

    @Schema(description = "税后月收入", example = "20000", required = true)
    @NotNull(message = "税后月收入不能为空")
    @DecimalMin(value = "0", inclusive = false, message = "月收入必须大于0")
    private BigDecimal salary;

    @Schema(description = "当前存款", example = "150000", required = true)
    @NotNull(message = "当前存款不能为空")
    @DecimalMin(value = "0", message = "存款不能小于0")
    private BigDecimal money;

    @Schema(description = "消费档位", example = "medium", allowableValues = {"low", "medium", "high"}, required = true)
    @NotBlank(message = "消费档位不能为空")
    private String consume;

    @Schema(description = "工作状态", example = "employee", allowableValues = {"employee", "freelancer", "business", "unemployed"}, required = true)
    @NotBlank(message = "工作状态不能为空")
    private String workType;
}
