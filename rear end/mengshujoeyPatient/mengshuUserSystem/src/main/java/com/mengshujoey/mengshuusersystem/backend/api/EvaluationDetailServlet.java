package com.mengshujoey.mengshuusersystem.backend.api;

import com.mengCommon.backend.form.QueryEvaluationDetailByUser;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengshujoey.mengshuusersystem.backend.service.EvaluationDetailInformationService;
import com.mengshujoey.mengshuusersystem.backend.service.LabelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * application name:mengshujoeyPatient - EvaluationDetailServlet
 * application describing:
 * copyright:
 * company:
 * time:2023-02-11 08:55:42
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@RestController
@RequestMapping("/evaluationDetail")
@Valid
public class EvaluationDetailServlet {
    @Autowired
    private EvaluationDetailInformationService evaluationDetailInformationService;
    @Autowired
    private LabelService labelService;

    /**
     * 获取标签
     *
     * @return
     */
    @ApiOperation(value = "获取标签")
    @GetMapping("/getLabelValue")
    public ResponseResult<String> getLabelValue() {
        return labelService.getLabelValue();
    }

    /**
     * 获取奶茶测评数据
     *
     * @return
     */
    @ApiOperation(value = "获取奶茶测评数据")
    @PostMapping("/getMilkTeaEvaluationData")
    public ResponseResult<String> getMilkTeaEvaluationData(@Valid @RequestBody QueryEvaluationDetailByUser evaluationDetail) {
        return evaluationDetailInformationService.getMilkTeaEvaluationData(evaluationDetail);
    }

    /**
     * 首页奶茶更新码
     *
     * @return
     */
    @ApiOperation(value = "首页奶茶更新码")
    @GetMapping("/milkTeaUpdateCode")
    public ResponseResult<String> milkTeaUpdateCode() {
        return evaluationDetailInformationService.milkTeaUpdateCode();
    }

    /**
     * 获取更新视频
     *
     * @param evaluationDetail
     * @return
     */
    @ApiOperation(value = "获取更新视频")
    @PostMapping("/findAllVideo")
    public ResponseResult<String> findAllVideo(@Valid @RequestBody QueryEvaluationDetailByUser evaluationDetail) {
        return evaluationDetailInformationService.findAllVideo(evaluationDetail);
    }

    /**
     * 首页奶茶更新码
     *
     * @return
     */
    @ApiOperation(value = "首页视频更新码")
    @GetMapping("/evaluationVideoUpdateCode")
    public ResponseResult<String> evaluationVideoUpdateCode() {
        return evaluationDetailInformationService.evaluationVideoUpdateCode();
    }

}
