package com.mengshujoey.mengshuusersystem.common.config.feign;

import org.springframework.cloud.openfeign.FeignClient;

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
@FeignClient(value = "library-system-admin", configuration = FeignLogConfig.class)
public interface librarySystemAdminFeignClient {
}