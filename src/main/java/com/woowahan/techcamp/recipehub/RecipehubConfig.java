package com.woowahan.techcamp.recipehub;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

public abstract class RecipehubConfig implements WebMvcConfigurer {

    @Configuration
    @Profile("development")
    static class RecipehubDevConfig extends RecipehubConfig {
    }

    @Configuration
    @Profile("test")
    static class RecipehubTestConfig extends RecipehubConfig {

    }

    @Configuration
    @Profile("production")
    static class RecipeHubProdConfig extends RecipehubConfig {

    }
}
