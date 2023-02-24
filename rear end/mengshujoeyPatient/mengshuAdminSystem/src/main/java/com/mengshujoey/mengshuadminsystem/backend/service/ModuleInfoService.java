package com.mengshujoey.mengshuadminsystem.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mengCommon.backend.form.ModuleQueryData;
import com.mengCommon.backend.pojo.ModuleInfo;
import com.mengCommon.common.common.http.ResponseResult;

import java.util.ArrayList;

/**
 * application name:mengshujoeyPatient - ModuleInfoService
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【module_info】的数据库操作Service
 * @createDate 2023-01-14 15:08:10
 * @since 1.8
 */
public interface ModuleInfoService extends IService<ModuleInfo> {

    ResponseResult<ArrayList<ModuleQueryData>> findModuleInfo();

}
