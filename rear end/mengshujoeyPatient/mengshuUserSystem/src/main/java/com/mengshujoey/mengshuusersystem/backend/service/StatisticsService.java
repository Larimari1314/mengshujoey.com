package com.mengshujoey.mengshuusersystem.backend.service;

import com.mengCommon.common.common.http.ResponseResult;

import javax.servlet.http.HttpServletRequest;

/**
 * application name:mengshujoeyPatient - StatisticsService
 * application describing:
 * copyright:
 * company:
 * time:2023-02-20 18:17:30
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
public interface StatisticsService {
    ResponseResult<String> countDailyActivity(HttpServletRequest request);

    ResponseResult<String> individualClicks(String projectId);
}
