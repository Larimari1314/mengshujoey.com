package com.mengshujoey.mengshuadminsystem.backend.api;

import com.github.pagehelper.PageInfo;
import com.mengCommon.backend.form.DefaultQueryForm;
import com.mengCommon.backend.form.DefaultSingleValue;
import com.mengCommon.backend.form.EditLabelData;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengshujoey.mengshuadminsystem.backend.service.LabelService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.LinkedList;
import java.util.Map;

/**
 * application name:mengshujoeyPatient - LabelServlet
 * application describing: 管理员标签管理API实现
 * copyright:
 * company:
 * time:2023-02-02 09:53:05
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@RestController
@RequestMapping("/labelManager")
@Valid
public class LabelServlet {
    @Autowired
    private LabelService labelService;

    /**
     * 查询当前标签数据
     *
     * @param defaultQueryForm
     * @return
     */
    @ApiOperation(value = "查询当前标签数据")
    @PreAuthorize("hasAnyAuthority('5','15')")
    @PostMapping("/queryLabelData")
    public ResponseResult<PageInfo<Map<String, Object>>> queryLabelData(@RequestBody @Valid DefaultQueryForm defaultQueryForm) {
        return labelService.queryLabelData(defaultQueryForm);
    }

    /**
     * 添加标签数据
     *
     * @param defaultSingleValue
     * @return
     */
    @ApiOperation(value = "添加标签数据")
    @PreAuthorize("hasAnyAuthority('5','16')")
    @PostMapping("/addLabelData")
    public ResponseResult<String> addLabelData(@RequestBody @Valid DefaultSingleValue defaultSingleValue, @RequestHeader(value = "token") String token) {
        return labelService.addLabelData(defaultSingleValue, token);
    }

    /**
     * 删除标签数据
     *
     * @param ids
     * @return
     */
    @ApiOperation(value = "删除标签数据")
    @PreAuthorize("hasAnyAuthority('5','17')")
    @PostMapping("/deleteLabelById")
    public ResponseResult<String> deleteLabel(@RequestBody LinkedList<Integer> ids, @RequestHeader(value = "token") String token) {
        return labelService.deleteLabel(ids, token);
    }

    /**
     * 修改标签数据
     *
     * @param editLabelData
     * @param token
     * @return
     */
    @ApiOperation(value = "修改标签数据")
    @PreAuthorize("hasAnyAuthority('5','18')")
    @PostMapping("/editLabelData")
    public ResponseResult<String> editLabelData(@RequestBody @Valid EditLabelData editLabelData, @RequestHeader(value = "token") String token) {
        return labelService.editLabelData(editLabelData, token);
    }
}
