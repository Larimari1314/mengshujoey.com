package com.mengshujoey.mengshuadminsystem.backend.api;

import com.mengCommon.common.common.http.ResponseResult;
import com.mengshujoey.mengshuadminsystem.backend.service.AdminLoginService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * application name:mengshujoeyPatient - AdminLoginServlet
 * application describing: 管理员登录
 * copyright:
 * company:
 * time:2023-01-15 14:44:54
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@RestController
@RequestMapping("/adminLogin")
public class AdminLoginServlet {
    @Autowired
    private AdminLoginService adminLoginService;

    /**
     * 获取公钥地址
     *
     * @return
     */
    @ApiOperation(value = "登录获取公钥地址")
    @GetMapping("/obtainPrivateKey")
    public ResponseResult<String> obtainPrivateKey() {
        return adminLoginService.obtainPublicKey();
    }
}
