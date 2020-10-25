package com.company.chat.config;

import com.company.chat.controller.HomeController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@ComponentScan(basePackageClasses = { HomeController.class })
public class WebConfig implements WebMvcConfigurer {

	// TODO: add swagger

}

