package com.woowahan.techcamp.recipehub;

import com.woowahan.techcamp.recipehub.common.security.AuthRequiredInterceptor;
import com.woowahan.techcamp.recipehub.common.security.BasicAuthInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public abstract class RecipehubConfig implements WebMvcConfigurer {

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
        }
    }

    @Configuration
    @Profile("production")
    static class RecipeHubProdConfig extends RecipehubConfig {

    }

    @Configuration
    @Profile("local")
    static class RecipeHubLocalConfig extends RecipehubConfig {

        @Bean
        public AuthRequiredInterceptor authRequiredInterceptor() {
            return new AuthRequiredInterceptor();
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            registry.addInterceptor(authRequiredInterceptor());
        }
    }
}
