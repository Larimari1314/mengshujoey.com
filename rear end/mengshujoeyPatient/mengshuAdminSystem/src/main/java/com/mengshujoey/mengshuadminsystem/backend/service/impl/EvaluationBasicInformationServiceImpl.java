package com.mengshujoey.mengshuadminsystem.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mengCommon.backend.dao.EvaluationBasicInformationMapper;
import com.mengCommon.backend.dao.EvaluationDetailInformationMapper;
import com.mengCommon.backend.dao.LabelEvaluationMapper;
import com.mengCommon.backend.form.*;
import com.mengCommon.backend.pojo.EvaluationBasicInformation;
import com.mengCommon.common.common.http.RabbitStatus;
import com.mengCommon.common.common.http.RedisCache;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengCommon.common.common.http.StatusCode;
import com.mengCommon.common.config.RabbitQueuesConfig;
import com.mengCommon.common.utils.IdWorker;
import com.mengCommon.common.utils.JwtUtils;
import com.mengshujoey.mengshuadminsystem.backend.service.AdminLogService;
import com.mengshujoey.mengshuadminsystem.backend.service.EvaluationBasicInformationService;
import com.mengshujoey.mengshuadminsystem.common.utils.ObjectUtils;
import com.mengshujoey.mengshuadminsystem.common.utils.ObsUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * application name:mengshujoeyPatient - EvaluationBasicInformationServiceImpl
 * application describing: 管理员端针对测评数据的service实现
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【evaluation_basic_information】的数据库操作Service实现
 * @createDate 2023-01-14 15:07:58
 * @since 1.8
 */
@Service
public class EvaluationBasicInformationServiceImpl extends ServiceImpl<EvaluationBasicInformationMapper, EvaluationBasicInformation> implements EvaluationBasicInformationService {
    @Autowired
    private ObsUtil obsUtil;
    @Autowired
    private EvaluationBasicInformationMapper evaluationBasicInformationMapper;

    @Autowired
    private LabelEvaluationMapper labelEvaluationMapper;

    @Autowired
    private EvaluationDetailInformationMapper evaluationDetailInformationMapper;
    @Autowired
    private ObjectUtils objectUtils;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private AdminLogService adminLogService;

    @Override
    public String uploadCoverAddress(MultipartFile file) {
        long fileName = new IdWorker().nextId();
        try {
            return obsUtil.uploadFile(file, "mengshujoey/projectImage/" + fileName + ".jpg");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String uploadEvaluationAddress(MultipartFile evaluationImage) {
        long fileName = new IdWorker().nextId();
        try {
            return obsUtil.uploadFile(evaluationImage, "mengshujoey/evaluationImage/" + fileName + ".jpg");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String uploadProjectAddress(MultipartFile projectImage) {
        long fileName = new IdWorker().nextId();
        try {
            return obsUtil.uploadFile(projectImage, "mengshujoey/projectImage/" + fileName + ".jpg");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public ResponseResult<String> addProject(AddBasicItem addBasicItem, String token) {
        //添加数据
        addBasicItem.setId(new IdWorker().nextId() + "");
        //添加基本数据
        evaluationBasicInformationMapper.addProject(addBasicItem);
        //添加书签数据
        labelEvaluationMapper.addLabelEvaluation(addBasicItem.getId(), addBasicItem.getLabelId());
        //添加项目数据
        for (ProjectItem projectItem : addBasicItem.getProjectItem()) {
            projectItem.setFraction((ArrayList<String>) objectUtils.fractionBySeven(projectItem.getFraction()));
            //添加数据
            evaluationDetailInformationMapper.addDetailProjectOne(projectItem, addBasicItem.getId());
        }
        //弃用
        // evaluationDetailInformationMapper.addDetailProject(addBasicItem.getProjectItem(),addBasicItem.getId());
        //记录日志
        adminLogService.recordOperationLog(JwtUtils.parseJWT(token), new Date(), "LE001", "against tables label_evaluation,evaluation_basic_information,evaluation_detail_information was add operation was performed");
        //rabbitMQ发送更新数据
        RabbitMessage rabbitMessage = null;
        if (Objects.equals(addBasicItem.getLabelId(), "1")) {
            //add删除缓存信息
            rabbitMessage = new RabbitMessage().setId(null).setRedisCache(Arrays.asList(RedisCache.MainEvaluationDetail.name(),RedisCache.evaluationBasic.name())).setStatus(RabbitStatus.ADD.name());
        } else {
            rabbitMessage = new RabbitMessage().setId(null).setRedisCache(Arrays.asList(RedisCache.evaluationBasic.name())).setStatus(RabbitStatus.ADD.name());
        }
        //发送霞浦县哦
        rabbitTemplate.convertAndSend("", RabbitQueuesConfig.USER_DATA_FLUSH, rabbitMessage);
        return ResponseResult.getSuccessResult(null, "The addition succeeded");
    }

    @Transactional
    @Override
    public ResponseResult<String> addSingleProject(AddSingleProject addSingleProject, String token) {
        //创建id
        addSingleProject.setId(new IdWorker().nextId() + "");
        //添加数据
        evaluationBasicInformationMapper.addProject(new AddBasicItem(addSingleProject.getId(), addSingleProject.getMainTitle(), addSingleProject.getSubheading(), addSingleProject.getEvaluationDate(), addSingleProject.getCoverImage(), addSingleProject.getVideoAddress(), addSingleProject.getLabelId()));
        //添加项目
        labelEvaluationMapper.addLabelEvaluation(addSingleProject.getId(), addSingleProject.getLabelId());
        //添加单个数据
        for (SingleProjectItem singleProjectItem : addSingleProject.getProjectItem()) {
            evaluationDetailInformationMapper.addDetailProjectSingle(singleProjectItem, addSingleProject.getId());
        }
        //记录日志
        adminLogService.recordOperationLog(JwtUtils.parseJWT(token), new Date(), "LE001", "An add operation was performed on table label_evaluation,evaluation_basic_information,evaluation_detail_information");
        //rabbitMQ发送更新数据
        RabbitMessage rabbitMessage = new RabbitMessage().setId(null).setRedisCache(Arrays.asList(RedisCache.evaluationBasic.name())).setStatus(RabbitStatus.ADD.name());
        //发送霞浦县哦
        rabbitTemplate.convertAndSend("", RabbitQueuesConfig.USER_DATA_FLUSH, rabbitMessage);
        return ResponseResult.getSuccessResult(null, "The personal assessment item was added successfully");
    }

    @Override
    public ResponseResult<PageInfo<Map<String, Object>>> queryAssessmentData(QueryAssessmentData assessmentData) {
        PageHelper.startPage(assessmentData.getPage(), 4);
        ArrayList<Map<String, Object>> arrayList = evaluationBasicInformationMapper.queryAssessmentData(assessmentData);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(arrayList);
        return ResponseResult.getSuccessResult(pageInfo, "The query of all assessment data is successful!");
    }

    @Transactional
    @Override
    public ResponseResult<String> deleteEvaluationProject(LinkedList<String> deleteList, String token) {
        if (deleteList.size() < 1) {
            return ResponseResult.getErrorResult("The current list is empty", StatusCode.NOT_MODIFIED, null);
        }
        int i = evaluationBasicInformationMapper.deleteByIds(deleteList);
        //记录日志
        adminLogService.recordOperationLog(JwtUtils.parseJWT(token), new Date(), "LE002", "A delete operation was performed on table evaluation_basic_information by id = " + StringUtils.join(deleteList, ','));
        //rabbitMQ发送更新数据
        RabbitMessage rabbitMessage = new RabbitMessage().setId(null).setRedisCache(Arrays.asList(RedisCache.evaluationBasic.name(),RedisCache.MainEvaluationDetail.name())).setStatus(RabbitStatus.DELETE.name());
        //发送数据
        rabbitTemplate.convertAndSend("", RabbitQueuesConfig.USER_DATA_FLUSH, rabbitMessage);
        if (i != 0) {
            return ResponseResult.getMessageResult(null, "The deletion of the assessment information is successful");
        }
        return ResponseResult.getErrorResult("Failed to delete assessment information", StatusCode.NOT_MODIFIED, null);
    }

    @Transactional
    @Override
    public ResponseResult<String> editBasicInformation(EditBasicInformation basicInformation, String token) {
//        EditBasicInformation(evaluationImage=, videoAddress=, labelId=1, mainTitle=, coverImage=, scoringClass=false, id=1617689718988935168, evaluationDateCode=, subheading=！)
        int i = evaluationBasicInformationMapper.editBasicInformation(basicInformation);
        labelEvaluationMapper.updateByBasicInformation(basicInformation.getId(), basicInformation.getLabelId());
        if (i == 0) {
            //更新失败
            return ResponseResult.getErrorResult("Failed to update basic assessment information", StatusCode.NOT_MODIFIED, null);
        }
        //记录日志
        adminLogService.recordOperationLog(JwtUtils.parseJWT(token), new Date(), "LE003", "A update operation was performed on table evaluation_basic_information by id = " + basicInformation.getId());
        //rabbitMQ发送更新数据
        RabbitMessage rabbitMessage = new RabbitMessage().setId(null).setRedisCache(Arrays.asList(RedisCache.evaluationBasic.name(),RedisCache.MainEvaluationDetail.name())).setStatus(RabbitStatus.UPDATE.name());
        RabbitMessage rabbitMessageTwo = new RabbitMessage().setId(Arrays.asList(basicInformation.getId())).setRedisCache(Arrays.asList(RedisCache.projectSingle.name())).setStatus(RabbitStatus.UPDATE.name());
        //发送数据
        rabbitTemplate.convertAndSend("", RabbitQueuesConfig.USER_DATA_FLUSH, rabbitMessage);
        rabbitTemplate.convertAndSend("", RabbitQueuesConfig.USER_DATA_FLUSH, rabbitMessageTwo);
        return ResponseResult.getSuccessResult(null, "The basic information of the assessment has been updated successfully");
    }

    @Override
    public ResponseResult<ArrayList<Map<String, Object>>> findDetailInformationById(String id) {
        ArrayList<Map<String, Object>> detailInformationById = evaluationDetailInformationMapper.findDetailInformationById(id);
        return ResponseResult.getSuccessResult(detailInformationById, "The query of detail information is success");
    }

    @Override
    public ResponseResult<ArrayList<Map<String, Object>>> getAllBasicMainTitle() {
        ArrayList<Map<String, Object>> mainTitleInformation = evaluationBasicInformationMapper.getAllBasicMainTitle();
        return ResponseResult.getSuccessResult(mainTitleInformation, "The query of detail main title is success");
    }

    @Override
    public ResponseResult<ArrayList<Map<String, Object>>> queryProjectData() {
        ArrayList<Map<String, Object>> arrayList = evaluationBasicInformationMapper.queryProjectData();
        return ResponseResult.getSuccessResult(arrayList, "query success");
    }
}




