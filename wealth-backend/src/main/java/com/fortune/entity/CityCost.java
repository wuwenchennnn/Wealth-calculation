package com.fortune.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("city_cost")
public class CityCost implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String cityName;
    private BigDecimal housePriceAvg;
    private BigDecimal rentPriceAvg;
    private BigDecimal mealCostMonthly;
    private BigDecimal transportCost;
    private BigDecimal peerAvgIncome;
    private BigDecimal consumeLevelFactor;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
