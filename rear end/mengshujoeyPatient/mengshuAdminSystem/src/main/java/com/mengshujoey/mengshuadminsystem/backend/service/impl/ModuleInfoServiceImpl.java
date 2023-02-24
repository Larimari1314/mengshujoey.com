package com.mengshujoey.mengshuadminsystem.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengCommon.backend.dao.ModuleInfoMapper;
import com.mengCommon.backend.form.ModuleQueryData;
import com.mengCommon.backend.pojo.ModuleInfo;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengshujoey.mengshuadminsystem.backend.service.ModuleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

/**
 * application name:mengshujoeyPatient - ModuleInfoServiceImpl
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【module_info】的数据库操作Service实现
 * @createDate 2023-01-14 15:08:10
 * @since 1.8
 */
@Service
public class ModuleInfoServiceImpl extends ServiceImpl<ModuleInfoMapper, ModuleInfo>
        implements ModuleInfoService {
    @Autowired
    private ModuleInfoMapper moduleInfoMapper;
    @Override
    public ResponseResult<ArrayList<ModuleQueryData>> findModuleInfo() {
        return ResponseResult.getSuccessResult(moduleInfoMapper.moduleQueryData(), "The list of permissions was queried successfully");
    }
}




