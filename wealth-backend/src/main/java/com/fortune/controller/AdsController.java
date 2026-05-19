package com.fortune.controller;

import com.fortune.common.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "广告配置", description = "小程序广告位配置接口")
@RestController
@RequestMapping("/api/ads")
public class AdsController {

    @Operation(summary = "获取广告配置", description = "返回激励视频、插屏和 Banner 广告位 ID")
    @GetMapping("/config")
    public ApiResponse<Map<String, String>> config() {
        Map<String, String> config = new HashMap<>();
        config.put("rewardAdUnitId", "adunit-demo-reward");
        config.put("interstitialAdUnitId", "adunit-demo-interstitial");
        config.put("bannerAdUnitId", "adunit-demo-banner");
        return ApiResponse.success(config);
    }
}
