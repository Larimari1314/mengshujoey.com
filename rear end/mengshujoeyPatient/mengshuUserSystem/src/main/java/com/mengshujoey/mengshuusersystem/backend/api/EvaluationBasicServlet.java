package com.mengshujoey.mengshuusersystem.backend.api;

import com.mengCommon.backend.form.QueryDataByUser;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengshujoey.mengshuusersystem.backend.service.EvaluationBasicInformationService;
import com.mengshujoey.mengshuusersystem.backend.service.StudioMemberService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * application name:mengshujoeyPatient - EvaluationBasicServlet
 * application describing:
 * copyright:
 * company:
 * time:2023-02-15 14:50:05
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@RestController
@RequestMapping("/evaluationBasic")
@Valid
public class EvaluationBasicServlet {
    @Autowired
    private EvaluationBasicInformationService evaluationBasicInformationService;
    @Autowired
    private StudioMemberService studioMemberService;

    /**
     * 查询单个页面数据
     *
     * @param basicId
     * @return
     */
    @ApiOperation(value = "查询单个页面数据")
    @GetMapping("/queryEvaluationBasic/{basicId}")
    public ResponseResult<String> queryEvaluationBasic(@PathVariable String basicId) {
        return evaluationBasicInformationService.queryEvaluationBasic(basicId);
    }

    /**
     * 获取工作室成员
     *
     * @return
     */
    @ApiOperation(value = "获取工作室成员")
    @GetMapping("/queryStudioMember")
    public ResponseResult<String> queryStudioMember() {
        return studioMemberService.queryStudioMember();
    }

    /**
     * 搜索信息
     *
     * @param data
     * @return
     */
    @ApiOperation(value = "搜索信息")
    @PostMapping("/queryData")
    public ResponseResult<String> queryData(@RequestBody @Valid QueryDataByUser data) {
        return evaluationBasicInformationService.queryData(data);
    }
}
