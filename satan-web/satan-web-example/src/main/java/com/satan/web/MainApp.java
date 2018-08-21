package com.satan.web;

import com.satan.common.web.AbstractMainApp;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.*;

/**
 * Created by huangpin on 17/3/16.
 */
@Slf4j
@Configuration
@EnableAutoConfiguration
@SpringBootApplication
@EnableCaching
@PropertySources(
        {
//                @PropertySource(value = "classpath:application-common.properties",ignoreResourceNotFound = false)
                @PropertySource(value = "classpath:application.properties",ignoreResourceNotFound = false)
        }
)
@ComponentScan(
        basePackages = {"com.satan"}
//        excludeFilters = {@ComponentScan.Filter(type = FilterType.REGEX,pattern = "com.satan.web.controller.*")}
)
public class MainApp extends AbstractMainApp {
    public static void main(String[] args) {
        context = SpringApplication.run(MainApp.class, args);
        for(String name : context.getBeanDefinitionNames()){
            log.info(name);
        }
    }
}
