package com.epam.esm.interceptor;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Component
@AllArgsConstructor
public class InterceptorConfig implements WebMvcConfigurer {

    private TagInterceptor tagInterceptor;
    private CertificateInterceptor certificateInterceptor;

    public void addInterceptors(InterceptorRegistry registry) {
        InterceptorRegistration tagInterceptorRegistration = registry.addInterceptor(tagInterceptor);
        tagInterceptorRegistration.addPathPatterns("/tags/**");
    }
}

