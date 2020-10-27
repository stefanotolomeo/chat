package com.company.chat.config;

import com.company.chat.dao.manager.MessageService;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@ComponentScan(basePackageClasses = { MessageService.class })
public class DaoConfig {
}
