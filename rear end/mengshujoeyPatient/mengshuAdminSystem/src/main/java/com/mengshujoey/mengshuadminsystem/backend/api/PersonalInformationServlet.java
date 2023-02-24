package com.mengshujoey.mengshuadminsystem.backend.api;

import com.mengCommon.backend.form.EditAdminInformation;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengshujoey.mengshuadminsystem.backend.service.AdminInformationService;
import com.mengshujoey.mengshuadminsystem.backend.service.AdminLoginService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.Map;

/**
 * application name:mengshujoeyPatient - PersonalInformationServlet
 * application describing: 管理员个人信息修改
 * copyright:
 * company:
 * time:2023-02-08 13:41:05
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@RestController
@RequestMapping("/personalInformation")
@Valid
public class PersonalInformationServlet {
    @Autowired
    private AdminInformationService adminInformationService;
    @Autowired
    private AdminLoginService adminLoginService;

    /**
     * 修改个人基本信息
     *
     * @param adminInformation
     * @return
     */
    @ApiOperation(value = "修改个人基本信息")
    @PreAuthorize("hasAnyAuthority('10')")
    @PostMapping("/editAdminInformation")
    public ResponseResult<String> editAdminInformation(@RequestBody EditAdminInformation adminInformation) {
        return adminInformationService.editAdminInformationByIndividual(adminInformation);
    }

    /**
     * 修改登录信息
     *
     * @param loginInformation
     * @return
     */
    @ApiOperation(value = "修改登录信息")
    @PreAuthorize("hasAnyAuthority('10')")
    @PostMapping("/modifyLoginInformation")
    public ResponseResult<String> modifyLoginInformation(@RequestBody Map<String, Object> loginInformation) {
        return adminLoginService.modifyLoginInformation(loginInformation);
    }
}
