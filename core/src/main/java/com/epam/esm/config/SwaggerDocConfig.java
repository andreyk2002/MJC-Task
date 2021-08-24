package com.epam.esm.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import static springfox.documentation.builders.PathSelectors.regex;

@Configuration
@EnableSwagger2
public class SwaggerDocConfig {

    private static final String ALL_CHILD = ".*";

    private ApiInfo metadata() {
        return new ApiInfoBuilder().title("Swagger Demo").description("API reference guide for developers")
                .termsOfServiceUrl("https://www.javacodegeeks.com/").contact(new Contact("", "", "mary.zheng@jcg.org"))
                .version("1.0").build();
    }

    @Bean
    public Docket itemApi() {
        return new Docket(DocumentationType.SWAGGER_2).groupName("Item API").apiInfo(metadata()).select()
                .paths(regex("/tags" + ALL_CHILD)).build();

    }

}
