package com.mengshujoey.mengshuusersystem.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mengCommon.backend.form.QueryEvaluationDetailByUser;
import com.mengCommon.backend.pojo.EvaluationDetailInformation;
import com.mengCommon.common.common.http.ResponseResult;

/**
 * application name:mengshujoeyPatient - EvaluationDetailInformationService
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【evaluation_detail_information】的数据库操作Service
 * @createDate 2023-01-14 15:08:01
 * @since 1.8
 */
public interface EvaluationDetailInformationService extends IService<EvaluationDetailInformation> {

    ResponseResult<String> getMilkTeaEvaluationData(QueryEvaluationDetailByUser evaluationDetail);

    ResponseResult<String> milkTeaUpdateCode();

    ResponseResult<String> findAllVideo(QueryEvaluationDetailByUser evaluationDetail);

    ResponseResult<String> evaluationVideoUpdateCode();

}
