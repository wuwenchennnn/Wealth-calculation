package com.fortune.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
@Schema(description = "城市生活成本响应")
public class CityCostResponse {
    @Schema(description = "城市名称", example = "上海")
    private String cityName;

    @Schema(description = "平均房价")
    private BigDecimal housePriceAvg;

    @Schema(description = "平均租金")
    private BigDecimal rentPriceAvg;

    @Schema(description = "月均餐饮成本")
    private BigDecimal mealCostMonthly;

    @Schema(description = "交通成本")
    private BigDecimal transportCost;

    @Schema(description = "同龄人平均收入")
    private BigDecimal peerAvgIncome;

    @Schema(description = "消费水平系数")
    private BigDecimal consumeLevelFactor;
}
