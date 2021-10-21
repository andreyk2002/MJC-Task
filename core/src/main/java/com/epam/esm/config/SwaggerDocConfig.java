package com.epam.esm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerDocConfig {

    private static final String ALL_CHILD = ".*";

    private ApiInfo metadata() {
        return new ApiInfoBuilder().title("Swagger Documentation")
                .version("1.0").build();
    }

    @Bean
    public Docket tagsApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("Gift certificate API").apiInfo(metadata()).select()
                .paths(regex("/" + ALL_CHILD)).build();

    }




}
