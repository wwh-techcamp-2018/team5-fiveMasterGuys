package com.woowahan.techcamp.recipehub;


import com.woowahan.techcamp.recipehub.common.config.RecipehubConfig;
import com.woowahan.techcamp.recipehub.common.security.BasicAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;

@Configuration
@Profile("test")
public class RecipehubTestConfig extends RecipehubConfig {
    @Bean
    public BasicAuthInterceptor basicAuthInterceptor() {
        return new BasicAuthInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(basicAuthInterceptor());
        super.addInterceptors(registry);
    }
}
