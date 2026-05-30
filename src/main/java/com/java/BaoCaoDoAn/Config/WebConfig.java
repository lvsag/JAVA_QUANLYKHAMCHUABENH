package com.java.BaoCaoDoAn.Config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private AdminInterceptor adminInterceptor;

    @Autowired
    private UserInterceptor userInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(adminInterceptor)
                // Added/updated: protect doctor workflow pages moved out of /admin/bac-si.
                .addPathPatterns("/admin/**", "/bac-si/**")
                .excludePathPatterns("/bac-si", "/bac-si/BS*"); // Exclude public doctor list and details
                
        registry.addInterceptor(userInterceptor)
                .addPathPatterns("/ho-so/**");
    }
}
