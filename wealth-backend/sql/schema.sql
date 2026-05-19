CREATE DATABASE IF NOT EXISTS wealth_fortune DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE wealth_fortune;

DROP TABLE IF EXISTS city_cost;
CREATE TABLE city_cost (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    city_name VARCHAR(64) NOT NULL COMMENT '城市名',
    house_price_avg DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '房价均价 元/平',
    rent_price_avg DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '房租均价 元/月',
    meal_cost_monthly DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '月均餐饮消费',
    transport_cost DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '通勤成本 元/月',
    peer_avg_income DECIMAL(12,2) NOT NULL DEFAULT 0 COMMENT '同龄人平均收入',
    consume_level_factor DECIMAL(6,2) NOT NULL DEFAULT 1 COMMENT '消费层级系数',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    UNIQUE KEY uk_city_name (city_name)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='城市生活成本表';

DROP TABLE IF EXISTS user_calculate_record;
CREATE TABLE user_calculate_record (
    id BIGINT PRIMARY KEY AUTO_INCREMENT COMMENT '主键ID',
    openid VARCHAR(128) NOT NULL COMMENT '用户openid',
    city VARCHAR(64) NOT NULL COMMENT '城市',
    age INT NOT NULL COMMENT '年龄',
    monthly_income DECIMAL(12,2) NOT NULL COMMENT '月收入',
    savings DECIMAL(12,2) NOT NULL COMMENT '存款',
    consume_level VARCHAR(32) NOT NULL COMMENT '消费档位',
    work_status VARCHAR(32) NOT NULL COMMENT '工作状态',
    summary_report TEXT NOT NULL COMMENT '精简报告',
    full_report MEDIUMTEXT NOT NULL COMMENT '完整报告',
    unlocked TINYINT NOT NULL DEFAULT 0 COMMENT '是否解锁完整版 0否 1是',
    create_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    update_time DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    KEY idx_openid_create_time (openid, create_time),
    KEY idx_city (city)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='用户测算记录表';

INSERT INTO city_cost (city_name, house_price_avg, rent_price_avg, meal_cost_monthly, transport_cost, peer_avg_income, consume_level_factor) VALUES
('北京', 65000, 5200, 2600, 500, 11500, 1.35),
('上海', 68000, 5400, 2700, 480, 11800, 1.38),
('广州', 38000, 3200, 2300, 360, 9000, 1.12),
('深圳', 72000, 5600, 2600, 420, 12000, 1.42),
('杭州', 42000, 3300, 2400, 350, 9800, 1.18),
('成都', 18000, 2200, 1900, 260, 7600, 0.92),
('武汉', 17000, 2100, 1850, 260, 7300, 0.90),
('南京', 30000, 2800, 2200, 300, 8800, 1.05),
('西安', 15000, 1800, 1700, 240, 6800, 0.82),
('重庆', 14000, 1800, 1750, 250, 7000, 0.84),
('天津', 26000, 2400, 2000, 300, 8000, 0.98),
('苏州', 28000, 2600, 2100, 280, 8500, 1.02),
('长沙', 13000, 1800, 1800, 240, 7000, 0.85),
('郑州', 13000, 1700, 1750, 230, 6800, 0.82),
('青岛', 21000, 2300, 2000, 260, 7600, 0.95),
('宁波', 25000, 2500, 2100, 280, 8500, 1.00),
('合肥', 18000, 2100, 1900, 250, 7600, 0.92),
('佛山', 17000, 2100, 1900, 260, 7200, 0.90),
('东莞', 22000, 2300, 2000, 280, 8200, 0.98),
('厦门', 45000, 3300, 2300, 280, 8500, 1.15),
('福州', 25000, 2300, 2000, 260, 7800, 0.98),
('济南', 17000, 2000, 1850, 240, 7200, 0.88),
('沈阳', 12000, 1700, 1700, 230, 6500, 0.78),
('大连', 16000, 1900, 1850, 240, 7000, 0.86),
('昆明', 13000, 1700, 1700, 220, 6400, 0.80),
('无锡', 19000, 2200, 1950, 260, 7800, 0.94),
('南昌', 12000, 1600, 1650, 220, 6200, 0.76),
('太原', 11000, 1500, 1600, 220, 6000, 0.74),
('贵阳', 10000, 1500, 1600, 210, 5800, 0.72),
('南宁', 11000, 1500, 1650, 220, 6000, 0.74),
('其他城市', 12000, 1500, 1600, 220, 5500, 0.75);
