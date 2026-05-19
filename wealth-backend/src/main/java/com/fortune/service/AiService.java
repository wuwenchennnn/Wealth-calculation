package com.fortune.service;

import com.fortune.dto.AiDebugResponse;
import com.fortune.dto.CalculateRequest;

public interface AiService {
    String generateReport(CalculateRequest request, String prompt);

    AiDebugResponse debugDeepSeek();
}
