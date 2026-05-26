package com.javaweb.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

<<<<<<< HEAD
import java.nio.file.Path;
import java.nio.file.Paths;

@Configuration
public class StaticResourcesWebConfiguration
        implements WebMvcConfigurer {
    @Value("${hansport.upload-file.base-path}")
    private String basePath;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadPath = Paths.get(basePath).toAbsolutePath().normalize();
        registry.addResourceHandler("/storage/**")
                .addResourceLocations(uploadPath.toUri().toString());
=======
@Configuration
public class StaticResourcesWebConfiguration
        implements WebMvcConfigurer {
    @Value("${hansport.upload-file.base-uri}")
    private String baseURI;

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/storage/**")
                .addResourceLocations(baseURI);
>>>>>>> f4b3851583e6f81662849e37f18856b9cedbe2cf
    }
}
