package com.vishalrashmika.sinso.api.Config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;

@Configuration
public class SwaggerUICustomizer {

    @Value("${app.doc_title:SINSO API Documentation}")
    private String appDocTitle;

    @Bean
    public GroupedOpenApi publicApi() {
        return GroupedOpenApi.builder()
                .group("sinso-api")
                .displayName(appDocTitle)
                .pathsToMatch("/v1/**")
                .build();
    }
}