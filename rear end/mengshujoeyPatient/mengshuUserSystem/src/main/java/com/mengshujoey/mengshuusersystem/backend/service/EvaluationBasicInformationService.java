package com.mengshujoey.mengshuusersystem.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mengCommon.backend.form.QueryDataByUser;
import com.mengCommon.backend.pojo.EvaluationBasicInformation;
import com.mengCommon.common.common.http.ResponseResult;

/**
 * application name:mengshujoeyPatient - EvaluationBasicInformationService
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【evaluation_basic_information】的数据库操作Service
 * @createDate 2023-01-14 15:07:58
 * @since 1.8
 */
public interface EvaluationBasicInformationService extends IService<EvaluationBasicInformation> {

    ResponseResult<String> queryEvaluationBasic(String basicId);

    ResponseResult<String> queryData(QueryDataByUser data);
}
