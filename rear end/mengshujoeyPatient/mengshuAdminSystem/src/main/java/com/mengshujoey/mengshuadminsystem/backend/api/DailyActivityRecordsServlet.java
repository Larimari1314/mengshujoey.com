package com.mengshujoey.mengshuadminsystem.backend.api;

import com.mengCommon.common.common.http.ResponseResult;
import com.mengshujoey.mengshuadminsystem.backend.service.DailyActivityRecordsService;
import com.mengshujoey.mengshuadminsystem.backend.service.EvaluationBasicInformationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

/**
 * application name:mengshujoeyPatient - DailyActivityRecordsServlet
 * application describing:
 * copyright:
 * company:
 * time:2023-02-21 10:30:11
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@RestController
@RequestMapping("/dailyActivity")
public class DailyActivityRecordsServlet {
    @Autowired
    private DailyActivityRecordsService dailyActivityRecordsService;
    @Autowired
    private EvaluationBasicInformationService evaluationBasicInformationService;
    /**
     * 日活量获取
     *
     * @return
     */
    @ApiOperation(value = "日活量获取")
    @PreAuthorize("hasAnyAuthority('10')")
    @GetMapping("/queryDailyActivity")
    public ResponseResult<ArrayList<Map<String, Object>>> queryDailyActivity() {
        return dailyActivityRecordsService.queryDailyActivity();
    }

    @ApiOperation(value = "获取项目点击量")
    @PreAuthorize("hasAnyAuthority('10')")
    @GetMapping("/queryProjectData")
    public ResponseResult<ArrayList<Map<String,Object>>> queryProjectData(){
        return evaluationBasicInformationService.queryProjectData();
    }
}
