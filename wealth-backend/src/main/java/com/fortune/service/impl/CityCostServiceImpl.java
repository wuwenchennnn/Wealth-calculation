package com.fortune.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.fortune.dto.CityCostResponse;
import com.fortune.entity.CityCost;
import com.fortune.mapper.CityCostMapper;
import com.fortune.service.CityCostService;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class CityCostServiceImpl implements CityCostService {

    private static final String DEFAULT_CITY = "其他城市";

    private final CityCostMapper cityCostMapper;
    private final Map<String, CityCostResponse> cityCache = new ConcurrentHashMap<>();

    public CityCostServiceImpl(CityCostMapper cityCostMapper) {
        this.cityCostMapper = cityCostMapper;
    }

    @Override
    public CityCostResponse getByCity(String city) {
        String normalizedCity = normalizeCity(city);
        return cityCache.computeIfAbsent(normalizedCity, this::queryCity);
    }

    @Override
    public List<String> listCities() {
        List<CityCost> costs = cityCostMapper.selectList(new LambdaQueryWrapper<CityCost>()
                .select(CityCost::getCityName));
        List<String> cities = new ArrayList<>();
        for (CityCost cost : costs) {
            if (!DEFAULT_CITY.equals(cost.getCityName())) {
                cities.add(cost.getCityName());
            }
        }
        cities.sort(Comparator.comparingInt(this::citySortWeight).thenComparing(String::compareTo));
        cities.add(DEFAULT_CITY);
        return cities;
    }

    private CityCostResponse queryCity(String city) {
        CityCost cost = findCity(city);
        if (cost == null && !DEFAULT_CITY.equals(city)) {
            cost = findCity(DEFAULT_CITY);
        }
        if (cost == null) {
            return defaultCityCost(city);
        }
        return toResponse(DEFAULT_CITY.equals(cost.getCityName()) && !DEFAULT_CITY.equals(city) ? city : cost.getCityName(), cost);
    }

    private CityCost findCity(String city) {
        return cityCostMapper.selectOne(new LambdaQueryWrapper<CityCost>()
                .eq(CityCost::getCityName, city)
                .last("limit 1"));
    }

    private CityCostResponse toResponse(String cityName, CityCost cost) {
        return CityCostResponse.builder()
                .cityName(cityName)
                .housePriceAvg(cost.getHousePriceAvg())
                .rentPriceAvg(cost.getRentPriceAvg())
                .mealCostMonthly(cost.getMealCostMonthly())
                .transportCost(cost.getTransportCost())
                .peerAvgIncome(cost.getPeerAvgIncome())
                .consumeLevelFactor(cost.getConsumeLevelFactor())
                .build();
    }

    private CityCostResponse defaultCityCost(String city) {
        return CityCostResponse.builder()
                .cityName(city)
                .housePriceAvg(new BigDecimal("12000"))
                .rentPriceAvg(new BigDecimal("1500"))
                .mealCostMonthly(new BigDecimal("1600"))
                .transportCost(new BigDecimal("220"))
                .peerAvgIncome(new BigDecimal("5500"))
                .consumeLevelFactor(new BigDecimal("0.75"))
                .build();
    }

    private String normalizeCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            return DEFAULT_CITY;
        }
        return city.trim();
    }

    private int citySortWeight(String city) {
        return switch (city) {
            case "北京" -> 1;
            case "上海" -> 2;
            case "广州" -> 3;
            case "深圳" -> 4;
            case "杭州" -> 5;
            case "成都" -> 6;
            case "武汉" -> 7;
            case "南京" -> 8;
            case "西安" -> 9;
            case "重庆" -> 10;
            default -> 100;
        };
    }
}
