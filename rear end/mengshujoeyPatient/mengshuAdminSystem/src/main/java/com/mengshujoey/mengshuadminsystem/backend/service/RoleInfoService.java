package com.mengshujoey.mengshuadminsystem.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mengCommon.backend.form.AddRoleInfo;
import com.mengCommon.backend.form.DefaultQueryForm;
import com.mengCommon.backend.form.EditRoleInfoForm;
import com.mengCommon.backend.form.QueryRoleData;
import com.mengCommon.backend.pojo.RoleInfo;
import com.mengCommon.common.common.http.ResponseResult;

import java.util.ArrayList;
import java.util.Map;

/**
 * application name:mengshujoeyPatient - RoleInfoService
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【role_info】的数据库操作Service
 * @createDate 2023-01-14 15:08:13
 * @since 1.8
 */
public interface RoleInfoService extends IService<RoleInfo> {

    ResponseResult<PageInfo<QueryRoleData>> findRoleList(DefaultQueryForm queryForm);

    ResponseResult<String> addRoleByAdmin(AddRoleInfo roleInfo,String token);

    ResponseResult<String> editRoleById(EditRoleInfoForm editRoleInfoForm, String token);

    ResponseResult<ArrayList<Map<String, Object>>> getRoleList();

}
