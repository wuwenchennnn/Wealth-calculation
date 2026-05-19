package com.fortune.service;

public interface RateLimitService {
    void checkDailyCalculate(String openid);

    void checkBurst(String key);
}
