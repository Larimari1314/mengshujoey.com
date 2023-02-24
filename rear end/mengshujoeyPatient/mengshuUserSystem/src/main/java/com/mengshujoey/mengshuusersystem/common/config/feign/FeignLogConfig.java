package com.mengshujoey.mengshuusersystem.common.config.feign;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

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
@Configuration
public class FeignLogConfig {
    /**
     * 日志记录
     *
     * @return
     */
    @Bean
    public Logger.Level level() {
        return Logger.Level.FULL;
    }
}
