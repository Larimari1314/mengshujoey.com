package com.mengshujoey.mengshusystemserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@EnableEurekaServer
@SpringBootApplication
public class MengshuSystemServerApplication {

    public static void main(String[] args) {
        SpringApplication.run(MengshuSystemServerApplication.class, args);
    }

}
