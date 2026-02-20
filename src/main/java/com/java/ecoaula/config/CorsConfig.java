package com.java.ecoaula.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.util.StringUtils;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.net.URI;
import java.util.Arrays;

@Configuration
public class CorsConfig implements WebMvcConfigurer {

    private final String[] allowedOrigins;

    public CorsConfig(@Value("${app.cors.allowed-origins:http://localhost:5173}") String allowedOrigins) {
        this.allowedOrigins = Arrays.stream(allowedOrigins.split(","))
                .map(String::trim)
                .map(CorsConfig::normalizeOrigin)
                .filter(StringUtils::hasText)
                .distinct()
                .toArray(String[]::new);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins(allowedOrigins)
                .allowedMethods("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS")
                .allowedHeaders("*")
                .exposedHeaders("*")
                .allowCredentials(true);
    }

    private static String normalizeOrigin(String value) {
        if (!StringUtils.hasText(value)) {
            return value;
        }

        String trimmed = value.trim();

        try {
            URI uri = URI.create(trimmed);
            if (uri.getScheme() != null && uri.getHost() != null) {
                StringBuilder normalized = new StringBuilder()
                        .append(uri.getScheme())
                        .append("://")
                        .append(uri.getHost());
                if (uri.getPort() != -1) {
                    normalized.append(":").append(uri.getPort());
                }
                return normalized.toString();
            }
        } catch (IllegalArgumentException ignored) {
            // Keep original value if it's not a valid URI.
        }

        while (trimmed.endsWith("/")) {
            trimmed = trimmed.substring(0, trimmed.length() - 1);
        }

        return trimmed;
    }
}
