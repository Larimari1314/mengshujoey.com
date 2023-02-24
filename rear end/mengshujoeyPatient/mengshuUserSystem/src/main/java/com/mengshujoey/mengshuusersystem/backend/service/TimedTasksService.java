package com.mengshujoey.mengshuusersystem.backend.service;

import com.mengCommon.common.common.http.ResponseResult;

/**
 * application name:mengshujoeyPatient - TimedTasksService
 * application describing:
 * copyright:
 * company:
 * time:2023-02-11 10:22:59
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
public interface TimedTasksService {

    void refreshEncryptedData();

    ResponseResult<String> obtainPrivateKey();

    void DailyActivityStatistics();

    void countIndividualClicks();

}
