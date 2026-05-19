package com.fortune;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@MapperScan("com.fortune.mapper")
@SpringBootApplication
public class WealthBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(WealthBackendApplication.class, args);
    }
}
