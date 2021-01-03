package com.seeker.fitness.all.config;

import com.seeker.fitness.all.interceptor.UserInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class InterceptorConfig extends WebMvcConfigurationSupport {
    @Autowired
    UserInterceptor userInterceptor;

    @Override
    protected void addInterceptors(InterceptorRegistry registry) {
        //添加不必拦截的url集合
        List<String> excludeUrls=new ArrayList<>();
        excludeUrls.add("/users/login");
        excludeUrls.add("/users/enrollUser");
        //设置需要拦截的url以及不必拦截的url
        registry.addInterceptor(userInterceptor).addPathPatterns("/**").excludePathPatterns(excludeUrls);
    }
}
