package com.mengshujoey.mengshuadminsystem.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mengCommon.backend.form.DefaultSingleValue;
import com.mengCommon.backend.form.ManagerLoginInformation;
import com.mengCommon.backend.pojo.AdminLogin;
import com.mengCommon.common.common.http.ResponseResult;

import java.util.Map;

/**
 * application name:mengshujoeyPatient - AdminLoginService
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【admin_login】的数据库操作Service
 * @createDate 2023-01-14 15:07:52
 * @since 1.8
 */
public interface AdminLoginService extends IService<AdminLogin> {

    ManagerLoginInformation getAdminInformation(String loginEmail);

    ResponseResult<String> obtainPublicKey();

    ResponseResult<String> resetPassword(Long id, DefaultSingleValue defaultSingleValue, String token);

    ResponseResult<String> modifyLoginInformation(Map<String, Object> loginInformation);

}
