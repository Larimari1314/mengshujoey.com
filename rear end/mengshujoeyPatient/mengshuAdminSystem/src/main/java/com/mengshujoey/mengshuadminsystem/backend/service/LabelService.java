package com.mengshujoey.mengshuadminsystem.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mengCommon.backend.form.DefaultQueryForm;
import com.mengCommon.backend.form.DefaultSingleValue;
import com.mengCommon.backend.form.EditLabelData;
import com.mengCommon.backend.pojo.Label;
import com.mengCommon.common.common.http.ResponseResult;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

/**
 * application name:mengshujoeyPatient - LabelService
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【label】的数据库操作Service
 * @createDate 2023-01-14 15:08:04
 * @since 1.8
 */
public interface LabelService extends IService<Label> {

    ResponseResult<ArrayList<Map<String, Object>>> tabDropDown();

    ResponseResult<PageInfo<Map<String, Object>>> queryLabelData(DefaultQueryForm defaultQueryForm);

    ResponseResult<String> addLabelData(DefaultSingleValue defaultSingleValue,String token);

    ResponseResult<String> deleteLabel(LinkedList<Integer> ids, String token);

    ResponseResult<String> editLabelData(EditLabelData editLabelData,String token);
}
