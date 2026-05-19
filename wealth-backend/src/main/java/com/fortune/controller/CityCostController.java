package com.fortune.controller;

import com.fortune.common.ApiResponse;
import com.fortune.dto.CityCostResponse;
import com.fortune.service.CityCostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import java.util.List;

@Tag(name = "城市成本", description = "城市生活成本查询接口")
@Validated
@RestController
@RequestMapping("/api/city")
public class CityCostController {

    private final CityCostService cityCostService;

    public CityCostController(CityCostService cityCostService) {
        this.cityCostService = cityCostService;
    }

    @Operation(summary = "查询城市列表", description = "返回已收录城市，最后一项为其他城市")
    @GetMapping("/list")
    public ApiResponse<List<String>> list() {
        return ApiResponse.success(cityCostService.listCities());
    }

    @Operation(summary = "查询城市生活成本", description = "根据城市名称查询成本、房价和消费等参考数据；未收录城市使用其他城市兜底")
    @GetMapping("/cost")
    public ApiResponse<CityCostResponse> cost(
            @Parameter(description = "城市名称", required = true, example = "上海")
            @RequestParam @NotBlank(message = "城市不能为空") String city) {
        return ApiResponse.success(cityCostService.getByCity(city));
    }
}
