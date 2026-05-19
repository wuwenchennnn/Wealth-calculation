package com.fortune.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@TableName("user_calculate_record")
public class UserCalculateRecord implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String openid;
    private String city;
    private Integer age;
    private BigDecimal monthlyIncome;
    private BigDecimal savings;
    private String consumeLevel;
    private String workStatus;
    private String summaryReport;
    private String fullReport;
    private Integer unlocked;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
