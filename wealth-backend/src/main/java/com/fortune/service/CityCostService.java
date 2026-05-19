package com.fortune.service;

import com.fortune.dto.CityCostResponse;

import java.util.List;

public interface CityCostService {
    CityCostResponse getByCity(String city);

    List<String> listCities();
}
