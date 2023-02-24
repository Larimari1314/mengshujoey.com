package com.mengshujoey.mengshuadminsystem.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mengCommon.backend.form.AddBasicItem;
import com.mengCommon.backend.form.AddSingleProject;
import com.mengCommon.backend.form.EditBasicInformation;
import com.mengCommon.backend.form.QueryAssessmentData;
import com.mengCommon.backend.pojo.EvaluationBasicInformation;
import com.mengCommon.common.common.http.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

/**
 * application name:mengshujoeyPatient - EvaluationBasicInformationService
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【evaluation_basic_information】的数据库操作Service
 * @createDate 2023-01-14 15:07:58
 * @since 1.8
 */
public interface EvaluationBasicInformationService extends IService<EvaluationBasicInformation> {

    String uploadCoverAddress(MultipartFile file);

    String uploadEvaluationAddress(MultipartFile evaluationImage);

    String uploadProjectAddress(MultipartFile projectImage);

    ResponseResult<String> addProject(AddBasicItem addBasicItem, String token);

    ResponseResult<String> addSingleProject(AddSingleProject addSingleProject, String token);

    ResponseResult<PageInfo<Map<String, Object>>> queryAssessmentData(QueryAssessmentData assessmentData);

    ResponseResult<String> deleteEvaluationProject(LinkedList<String> deleteList,String token);

    ResponseResult<String> editBasicInformation(EditBasicInformation basicInformation, String token);

    ResponseResult<ArrayList<Map<String, Object>>> findDetailInformationById(String id);

    ResponseResult<ArrayList<Map<String, Object>>> getAllBasicMainTitle();

    ResponseResult<ArrayList<Map<String, Object>>> queryProjectData();

}
