package com.mengshujoey.mengshuusersystem.backend.api;

import com.mengCommon.common.common.http.ResponseResult;
import com.mengshujoey.mengshuusersystem.backend.service.TimedTasksService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * application name:mengshujoeyPatient - InitializeServlet
 * application describing:
 * copyright:
 * company:
 * time:2023-02-11 10:41:51
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@RestController
@RequestMapping("/initialize")
public class InitializeServlet {
    @Autowired
    private TimedTasksService timedTasksService;
    /**
     * 首次进入页面获取公钥
     * @return
     */
    @ApiOperation(value = "首次进入页面获取私钥")
    @GetMapping("/obtainPrivateKey")
    public ResponseResult<String> obtainPrivateKey(){
        return timedTasksService.obtainPrivateKey();
    }
}
