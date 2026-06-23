package com.servicedesk.servicedesk_pro.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("ServiceDesk Pro API")
                        .version("1.0")
                        .description("Issue Resolution & Workflow Management Platform")
                        .contact(new Contact()
                                .name("Dudekula Abdul Suban")
                                .email("227003171@sastra.ac.in")));
    }
}