package com.octaviookumu.tinylink.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.Arrays;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    private final String[] allowedOrigins;

    // allowedOrigins.split(",") splits the string at each comma.
    // "http://localhost:4200, https://example.com" becomes an array with two strings.
    // Arrays.stream(...) lets you process those strings one by one.
    // .map(String::trim) removes whitespace from the start and end of each string.
    // " https://example.com" becomes "https://example.com".
    // .toArray(String[]::new) collects the processed strings into a new String[]
    public WebConfig(@Value("${app.cors.allowed-origins}") String allowedOrigins) {
        this.allowedOrigins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .toArray(String[]::new);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOriginPatterns(allowedOrigins)
                .allowedMethods("GET", "POST", "DELETE", "OPTIONS")
                .allowedHeaders("*");
    }

}
