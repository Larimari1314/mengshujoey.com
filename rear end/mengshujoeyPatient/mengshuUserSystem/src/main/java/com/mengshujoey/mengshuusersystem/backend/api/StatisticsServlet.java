package com.mengshujoey.mengshuusersystem.backend.api;

import com.mengCommon.common.common.http.ResponseResult;
import com.mengshujoey.mengshuusersystem.backend.service.StatisticsService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * application name:mengshujoeyPatient - StatisticsServlet
 * application describing:
 * copyright:
 * company:
 * time:2023-02-20 10:40:23
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@RequestMapping("/statistics")
@RestController
public class StatisticsServlet {
    @Autowired
    private StatisticsService statisticsService;

    /**
     * 统计日活量
     *
     * @param request
     */
    @ApiOperation(value = "统计日活量")
    @GetMapping("/countDailyActivity")
    public ResponseResult<String> countDailyActivity(HttpServletRequest request) {
        return statisticsService.countDailyActivity(request);
    }

    /**
     * 统计项目点击量
     *
     * @param projectId
     * @return
     */
    @ApiOperation(value = "统计项目点击量")
    @GetMapping("/individualClicks/{projectId}")
    public ResponseResult<String> individualClicks(@PathVariable String projectId) {
        return statisticsService.individualClicks(projectId);
    }
}
