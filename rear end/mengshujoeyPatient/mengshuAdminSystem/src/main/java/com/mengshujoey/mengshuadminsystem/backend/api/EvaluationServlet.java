package com.mengshujoey.mengshuadminsystem.backend.api;

import com.github.pagehelper.PageInfo;
import com.mengCommon.backend.form.*;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengshujoey.mengshuadminsystem.backend.service.EvaluationBasicInformationService;
import com.mengshujoey.mengshuadminsystem.backend.service.EvaluationDetailInformationService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

/**
 * application name:mengshujoeyPatient - EvaluationServlet
 * application describing: 测评项目接口
 * copyright:
 * company:
 * time:2023-01-22 12:41:16
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@RestController
@Valid
@RequestMapping("/evaluation")
public class EvaluationServlet {
    @Autowired
    private EvaluationBasicInformationService evaluationBasicInformationService;
    @Autowired
    private EvaluationDetailInformationService evaluationDetailInformationService;

    /**
     * 上传封面地址
     *
     * @param coverImage
     * @return
     */
    @ApiOperation(value = "上传封面地址")
    @PreAuthorize("hasAnyAuthority('3' , '12' , '14')")
    @PostMapping("/uploadCoverAddress")
    public String uploadCoverAddress(@RequestBody MultipartFile coverImage) {
        return evaluationBasicInformationService.uploadCoverAddress(coverImage);
    }

    /**
     * 上传测评地址
     *
     * @param evaluationImage
     * @return
     */
    @ApiOperation(value = "上传测评图片地址")
    @PreAuthorize("hasAnyAuthority('3' , '12' , '14')")
    @PostMapping("/uploadEvaluationAddress")
    public String uploadEvaluationAddress(@RequestBody MultipartFile evaluationImage) {
        return evaluationBasicInformationService.uploadEvaluationAddress(evaluationImage);
    }

    /**
     * 上传
     *
     * @param projectImage
     * @return
     */
    @ApiOperation(value = "上传项目图片地址")
    @PreAuthorize("hasAnyAuthority('3' , '12' , '14')")
    @PostMapping("/uploadProjectAddress")
    public String uploadProjectAddress(@RequestBody MultipartFile projectImage) {
        return evaluationBasicInformationService.uploadProjectAddress(projectImage);
    }

    /**
     * 添加项目
     *
     * @param addBasicItem
     * @return
     */
    @ApiOperation(value = "添加项目")
    @PreAuthorize("hasAnyAuthority('3' , '12')")
    @PostMapping("/addProject")
    public ResponseResult<String> addProject(@RequestBody @Valid AddBasicItem addBasicItem, @RequestHeader(value = "token") String token) {
        return evaluationBasicInformationService.addProject(addBasicItem, token);
    }

    /**
     * 添加单人测评项目
     *
     * @param addSingleProject
     * @return
     */
    @ApiOperation(value = "添加单人测评项目")
    @PreAuthorize("hasAnyAuthority('3' , '12')")
    @PostMapping("/addSingleProject")
    public ResponseResult<String> addSingleProject(@RequestBody @Valid AddSingleProject addSingleProject, @RequestHeader(value = "token") String token) {
        return evaluationBasicInformationService.addSingleProject(addSingleProject, token);
    }

    /**
     * 查询全部测评数据
     *
     * @param assessmentData
     * @return
     */
    @ApiOperation(value = "查询全部测评数据")
    @PreAuthorize("hasAnyAuthority('3' , '11')")
    @PostMapping("/queryAssessmentData")
    public ResponseResult<PageInfo<Map<String, Object>>> queryAssessmentData(@RequestBody @Valid QueryAssessmentData assessmentData) {
        return evaluationBasicInformationService.queryAssessmentData(assessmentData);
    }

    /**
     * 删除测评信息
     *
     * @param deleteList
     * @return
     */
    @ApiOperation(value = "删除测评信息")
    @PreAuthorize("hasAnyAuthority('3' , '13')")
    @PostMapping("/deleteEvaluationProject")
    public ResponseResult<String> deleteEvaluationProject(@RequestBody LinkedList<String> deleteList, @RequestHeader(value = "token") String token) {
        return evaluationBasicInformationService.deleteEvaluationProject(deleteList, token);
    }

    /**
     * 修改基本信息
     *
     * @param basicInformation
     * @return
     */
    @ApiOperation(value = "修改基本信息")
    @PreAuthorize("hasAnyAuthority('3' , '14')")
    @PostMapping("/editBasicInformation")
    public ResponseResult<String> editBasicInformation(@RequestBody @Valid EditBasicInformation basicInformation, @RequestHeader(value = "token") String token) {
        return evaluationBasicInformationService.editBasicInformation(basicInformation, token);
    }

    /**
     * 根据id获取项目数据
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "根据id获取项目数据")
    @PreAuthorize("hasAnyAuthority('3' , '14')")
    @GetMapping("/findDetailInformation/{id}")
    public ResponseResult<ArrayList<Map<String, Object>>> findDetailInformationById(@PathVariable String id) {
        return evaluationBasicInformationService.findDetailInformationById(id);
    }

    /**
     * 修改项目基本信息
     *
     * @param editDetailInformation
     * @return
     */
    @ApiOperation(value = "修改项目基本信息")
    @PreAuthorize("hasAnyAuthority('3' , '14')")
    @PostMapping("/editDetailInformation")
    public ResponseResult<String> editDetailInformation(@RequestBody @Valid EditDetailInformation editDetailInformation, @RequestHeader(value = "token") String token) {
        return evaluationDetailInformationService.editDetailInformation(editDetailInformation, token);
    }

    /**
     * 删除基本项目信息
     *
     * @param id
     * @return
     */
    @ApiOperation(value = "删除基本项目信息")
    @PreAuthorize("hasAnyAuthority('3' , '13')")
    @DeleteMapping("/deleteDetailInformationById/{id}")
    public ResponseResult<String> deleteDetailInformationById(@PathVariable @NotNull(message = "id不得为空") Integer id, @RequestHeader(value = "token") String token) {
        return evaluationDetailInformationService.deleteDetailInformationById(id, token);
    }

    /**
     * 添加项目信息
     *
     * @param addDetailInformation
     * @return
     */
    @ApiOperation(value = "添加项目信息")
    @PreAuthorize("hasAnyAuthority('3' , '12')")
    @PostMapping("/addDetailInformation")
    public ResponseResult<String> addDetailInformation(@RequestBody @Valid AddDetailInformation addDetailInformation, @RequestHeader(value = "token") String token) {
        return evaluationDetailInformationService.addDetailInformation(addDetailInformation, token);
    }

}
