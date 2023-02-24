package com.mengshujoey.mengshuusersystem.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mengCommon.backend.pojo.Label;
import com.mengCommon.common.common.http.ResponseResult;

/**
 * application name:mengshujoeyPatient - LabelService
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【label】的数据库操作Service
 * @createDate 2023-01-14 15:08:04
 * @since 1.8
 */
public interface LabelService extends IService<Label> {

    ResponseResult<String> getLabelValue();

}
