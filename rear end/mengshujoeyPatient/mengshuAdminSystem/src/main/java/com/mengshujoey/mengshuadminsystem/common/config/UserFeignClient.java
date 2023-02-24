package com.mengshujoey.mengshuadminsystem.common.config;

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
@FeignClient(value = "library-system-reader", configuration = FeignLogConfig.class)
public interface UserFeignClient {
   /* @GetMapping("/librarySystemReader/dropdownListData/getBookType")
    public ResponseResult<ArrayList<Map<String, Object>>> getBookType();*/
}
