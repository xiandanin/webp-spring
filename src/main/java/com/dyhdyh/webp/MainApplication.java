package com.dyhdyh.webp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

/**
 * author  dengyuhan
 * created 2018/6/20 14:03
 */
@EnableAutoConfiguration
@SpringBootApplication
@ComponentScan(basePackages = {"com.dyhdyh.webp.controller", "com.dyhdyh.webp.service", "com.dyhdyh.webp.config"})
public class MainApplication extends SpringApplication{

    public static void main(String[] args) throws Exception {
        SpringApplication.run(MainApplication.class, args);
    }
}