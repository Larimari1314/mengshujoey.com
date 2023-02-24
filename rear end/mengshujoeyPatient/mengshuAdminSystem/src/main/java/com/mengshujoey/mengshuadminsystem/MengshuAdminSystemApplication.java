package com.mengshujoey.mengshuadminsystem;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.netflix.hystrix.dashboard.EnableHystrixDashboard;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@EnableAsync
@EnableEurekaClient
@EnableFeignClients
@EnableDiscoveryClient
@EnableHystrixDashboard // 开启Hystrix仪表盘监控功能
@PropertySource(value = {"classpath:/token.properties", "classpath:/snowflake.properties", "classpath:/obsHuaWei.properties", "classpath:/frontEndInterface.properties"})
@MapperScan(basePackages = ("com.mengCommon.backend.dao"))
@ComponentScan({"com.mengCommon", "com.mengshujoey.mengshuadminsystem"})
//@ComponentScan({"com.mengshujoey"})
@EnableScheduling
@SpringBootApplication
public class MengshuAdminSystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MengshuAdminSystemApplication.class, args);
    }


}
