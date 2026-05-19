package com.fortune.service;

import com.fortune.dto.CalculateRequest;
import com.fortune.dto.CalculateResponse;

public interface CalculateService {
    CalculateResponse calculate(CalculateRequest request);

    CalculateResponse unlock(Long recordId, String openid);
}
