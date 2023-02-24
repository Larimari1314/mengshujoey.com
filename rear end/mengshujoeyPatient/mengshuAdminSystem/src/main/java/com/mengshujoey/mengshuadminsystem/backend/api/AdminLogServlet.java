package com.mengshujoey.mengshuadminsystem.backend.api;

import com.github.pagehelper.PageInfo;
import com.mengCommon.backend.form.QueryEvaluationDetailByUser;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengshujoey.mengshuadminsystem.backend.service.AdminLogService;
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
 * application name:mengshujoeyPatient - AdminLogServlet
 * application describing:
 * copyright:
 * company:
 * time:2023-02-21 09:41:28
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@RestController
@RequestMapping("/adminLog")
public class AdminLogServlet {
    @Autowired
    private AdminLogService adminLogService;

    /**
     * 查询日志
     *
     * @param queryEvaluationDetail
     * @return
     */
    @ApiOperation(value = "查询日志")
    @PreAuthorize("hasAnyAuthority('1')")
    @PostMapping("/findAdminLog")
    public ResponseResult<PageInfo<Map<String, Object>>> findAdminLog(@RequestBody @Valid QueryEvaluationDetailByUser queryEvaluationDetail) {
        return adminLogService.findAdminLog(queryEvaluationDetail);
    }

}
