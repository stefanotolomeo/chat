package com.company.chat.config;

import com.company.chat.controller.HomeController;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { HomeController.class })
public class WebConfig {

}