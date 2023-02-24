package com.mengshujoey.mengshuadminsystem.backend.api;

import com.mengCommon.common.common.http.ResponseResult;
import com.mengshujoey.mengshuadminsystem.backend.service.ConstantItemService;
import com.mengshujoey.mengshuadminsystem.backend.service.EvaluationBasicInformationService;
import com.mengshujoey.mengshuadminsystem.backend.service.LabelService;
import com.mengshujoey.mengshuadminsystem.backend.service.StudioMemberService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.Map;

/**
 * application name:mengshujoeyPatient - DropDownOptionServlet
 * application describing: 下拉框接口
 * copyright:
 * company:
 * time:2023-01-23 07:56:56
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@RestController
@RequestMapping("/dropDownOption")
public class DropDownOptionServlet {
    @Autowired
    private LabelService labelService;
    @Autowired
    private ConstantItemService constantItemService;

    @Autowired
    private StudioMemberService studioMemberService;
    @Autowired
    private EvaluationBasicInformationService evaluationBasicInformationService;

    /**
     * 获取标签下拉框
     *
     * @return
     */
    @ApiOperation(value = "获取标签下拉框")
    @PreAuthorize("hasAnyAuthority('10')")
    @GetMapping("/tabDropDown")
    public ResponseResult<ArrayList<Map<String, Object>>> tabDropDown() {
        return labelService.tabDropDown();
    }

    /**
     * 获取推荐等级下拉列表
     *
     * @return
     */
    @ApiOperation(value = "获取推荐等级下拉列表")
    @PreAuthorize("hasAnyAuthority('10')")
    @GetMapping("/getReferralLevel")
    public ResponseResult<ArrayList<Map<String, Object>>> getReferralLevel() {
        return constantItemService.getReferralLevel();
    }

    /**
     * 获取性别下拉列表
     *
     * @return
     */
    @ApiOperation(value = "获取性别下拉列表")
    @PreAuthorize("hasAnyAuthority('10')")
    @GetMapping("/getSexList")
    public ResponseResult<ArrayList<Map<String, Object>>> getSexList() {
        return constantItemService.getSexList();
    }
    /**
     * 获取工作室成员信息
     *
     * @return
     */
    @ApiOperation(value = "获取工作室成员信息")
    @PreAuthorize("hasAnyAuthority('10')")
    @GetMapping("/getStudioMember")
    public ResponseResult<ArrayList<Map<String, Object>>> getStudioMember() {
        return studioMemberService.getStudioMember();
    }

    /**
     * 获取全部主标题数据
     *
     * @return
     */
    @ApiOperation(value = "获取全部主标题数据")
    @PreAuthorize("hasAnyAuthority('10')")
    @GetMapping("/getAllBasicMainTitle")
    public ResponseResult<ArrayList<Map<String, Object>>> getAllBasicMainTitle() {
        return evaluationBasicInformationService.getAllBasicMainTitle();
    }
}
