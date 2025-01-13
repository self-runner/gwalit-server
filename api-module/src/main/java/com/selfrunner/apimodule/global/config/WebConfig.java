package com.selfrunner.apimodule.global.config;

import com.selfrunner.apimodule.global.auth.AuthAuthorizationArgumentResolver;
import com.selfrunner.apimodule.global.sentry.SentryInterceptor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
@RequiredArgsConstructor
public class WebConfig implements WebMvcConfigurer {

    private final AuthAuthorizationArgumentResolver authAuthorizationArgumentResolver;

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> handlerMethodArgumentResolverList) {
        handlerMethodArgumentResolverList.add(authAuthorizationArgumentResolver);
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new SentryInterceptor());
    }
}
