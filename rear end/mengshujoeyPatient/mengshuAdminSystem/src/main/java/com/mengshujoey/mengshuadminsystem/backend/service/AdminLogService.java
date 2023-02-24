package com.mengshujoey.mengshuadminsystem.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mengCommon.backend.form.QueryEvaluationDetailByUser;
import com.mengCommon.backend.pojo.AdminLog;
import com.mengCommon.common.common.http.ResponseResult;

import java.util.Date;
import java.util.Map;

/**
 * application name:mengshujoeyPatient - AdminLogService
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【admin_log】的数据库操作Service
 * @createDate 2023-01-14 15:07:49
 * @since 1.8
 */
public interface AdminLogService extends IService<AdminLog> {
    void recordOperationLog(String adminId, Date operationTime,String level,String content);

    ResponseResult<PageInfo<Map<String, Object>>> findAdminLog(QueryEvaluationDetailByUser queryEvaluationDetail);

}
