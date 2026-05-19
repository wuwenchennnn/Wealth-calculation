package com.fortune.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI wealthOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("人生财富宿命测算后端 API")
                        .description("用于小程序端财富测算、报告解锁、城市成本和广告配置的接口文档")
                        .version("1.0.0")
                        .contact(new Contact().name("wealth-backend")));
    }
}
