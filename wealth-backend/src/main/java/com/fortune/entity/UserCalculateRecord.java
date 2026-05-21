package com.fortune.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
@TableName("user_calculate_record")
public class UserCalculateRecord implements Serializable {

    @TableId(type = IdType.AUTO)
    private Long id;
    private String openid;
    private String city;
    private Integer age;
    private String currentStatus;
    private String disposableRange;
    private String existingSavings;
    private String lifeRhythm;
    private String spendingHabit;
    private String actionStyle;
    private String summaryReport;
    private String fullReport;
    private Integer unlocked;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
