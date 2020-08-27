package com.converter;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.ComponentScan;

@SpringBootApplication
@ComponentScan({"com.converter.model"})
@ComponentScan({"com.converter"})
public class ConverterApplication extends SpringBootServletInitializer {
	
    /**
     * Used when run as JAR
     */
    public static void main(String[] args) {
        SpringApplication.run(ConverterApplication.class, args);
    }
    /**
     * Used when run as WAR
     */
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        return builder.sources(ConverterApplication.class);
    }
	
}
