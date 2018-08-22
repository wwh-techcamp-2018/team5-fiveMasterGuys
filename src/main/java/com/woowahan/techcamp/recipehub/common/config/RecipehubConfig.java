package com.woowahan.techcamp.recipehub.common.config;

import com.woowahan.techcamp.recipehub.common.security.AuthRequiredArgumentResolver;
import com.woowahan.techcamp.recipehub.common.security.AuthRequiredInterceptor;
import com.woowahan.techcamp.recipehub.common.security.BasicAuthInterceptor;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

public abstract class RecipehubConfig implements WebMvcConfigurer {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(30);
        return messageSource;
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource);
    }

    @Bean
    public AuthRequiredInterceptor authRequiredInterceptor() {
        return new AuthRequiredInterceptor();
    }

    @Bean
    public AuthRequiredArgumentResolver authRequiredArgumentResolver() {
        return new AuthRequiredArgumentResolver();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(authRequiredInterceptor());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
        resolvers.add(authRequiredArgumentResolver());
    }

    @Configuration
    @Profile("development")
    static class RecipehubDevConfig extends RecipehubConfig {
    }

    @Configuration
    @Profile("test")
    static class RecipehubTestConfig extends RecipehubConfig {
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

    @Configuration
    @Profile("production")
    static class RecipeHubProdConfig extends RecipehubConfig {

    }

    @Configuration
    @Profile("local")
    static class RecipeHubLocalConfig extends RecipehubConfig {
    }
}
