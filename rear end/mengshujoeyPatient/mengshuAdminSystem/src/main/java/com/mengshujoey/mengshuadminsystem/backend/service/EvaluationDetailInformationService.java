package com.mengshujoey.mengshuadminsystem.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mengCommon.backend.form.AddDetailInformation;
import com.mengCommon.backend.form.EditDetailInformation;
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

    ResponseResult<String> editDetailInformation(EditDetailInformation editDetailInformation,String token);

    ResponseResult<String> deleteDetailInformationById(Integer id,String token);

    ResponseResult<String> addDetailInformation(AddDetailInformation addDetailInformation,String token);
}
