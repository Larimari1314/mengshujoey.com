package com.mengshujoey.mengshuadminsystem.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mengCommon.backend.dao.LabelEvaluationMapper;
import com.mengCommon.backend.dao.LabelMapper;
import com.mengCommon.backend.form.DefaultQueryForm;
import com.mengCommon.backend.form.DefaultSingleValue;
import com.mengCommon.backend.form.EditLabelData;
import com.mengCommon.backend.form.RabbitMessage;
import com.mengCommon.backend.pojo.Label;
import com.mengCommon.common.common.http.RabbitStatus;
import com.mengCommon.common.common.http.RedisCache;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengCommon.common.common.http.StatusCode;
import com.mengCommon.common.config.RabbitQueuesConfig;
import com.mengCommon.common.utils.JwtUtils;
import com.mengshujoey.mengshuadminsystem.backend.service.AdminLogService;
import com.mengshujoey.mengshuadminsystem.backend.service.LabelService;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * application name:mengshujoeyPatient - LabelServiceImpl
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【label】的数据库操作Service实现
 * @createDate 2023-01-14 15:08:04
 * @since 1.8
 */
@Service
public class LabelServiceImpl extends ServiceImpl<LabelMapper, Label>
        implements LabelService {
    @Autowired
    private LabelMapper labelMapper;
    @Autowired
    private AdminLogService adminLogService;
    @Autowired
    private LabelEvaluationMapper labelEvaluationMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Override
    public ResponseResult<ArrayList<Map<String, Object>>> tabDropDown() {
        ArrayList<Map<String, Object>> tabValues = labelMapper.tabDropDown();
        return ResponseResult.getSuccessResult(tabValues, "Get drop-down list information successfully");
    }

    @Override
    public ResponseResult<PageInfo<Map<String, Object>>> queryLabelData(DefaultQueryForm defaultQueryForm) {
        PageHelper.startPage(defaultQueryForm.getPage(), 5);
        ArrayList<Map<String, Object>> labelList = labelMapper.queryLabelData(defaultQueryForm);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(labelList);
        return ResponseResult.getSuccessResult(pageInfo, "The query label data succeeded");
    }

    @Transactional
    @Override
    public ResponseResult<String> addLabelData(DefaultSingleValue defaultSingleValue, String token) {
        Integer labelId = labelMapper.addLabelData(defaultSingleValue);
        adminLogService.recordOperationLog(JwtUtils.parseJWT(token), new Date(), "LE001", "An add operation was performed on table label by id = " + labelId);
        RabbitMessage rabbitMessage = new RabbitMessage().setId(null).setRedisCache(Arrays.asList(RedisCache.labelValue.name())).setStatus(RabbitStatus.UPDATE.name());
        //发送消息
        rabbitTemplate.convertAndSend("", RabbitQueuesConfig.USER_DATA_FLUSH, rabbitMessage);
        return ResponseResult.getSuccessResult(null, "The tag value was added successfully");
    }

    @Override
    public ResponseResult<String> deleteLabel(LinkedList<Integer> ids, String token) {
        //检测删除条件
        Integer exists = labelEvaluationMapper.isExists(ids);
        if (exists > 0) {
            return ResponseResult.getErrorResult("Bind values exist in the deleted list", StatusCode.Not_Acceptable, null);
        }
        //删除数据
        labelMapper.deleteByIds(ids);
        //记录日志
        adminLogService.recordOperationLog(JwtUtils.parseJWT(token), new Date(), "LE002", "A delete operation was performed on table label by id = " + StringUtils.join(ids, ','));
        RabbitMessage rabbitMessage = new RabbitMessage().setId(null).setRedisCache(Arrays.asList(RedisCache.labelValue.name())).setStatus(RabbitStatus.DELETE.name());
        rabbitTemplate.convertAndSend("", RabbitQueuesConfig.USER_DATA_FLUSH, rabbitMessage);
        return ResponseResult.getSuccessResult(null, "Delete label data was successful");
    }

    @Transactional
    @Override
    public ResponseResult<String> editLabelData(EditLabelData editLabelData, String token) {
        //修改数据
        labelMapper.editLabelData(editLabelData);
        RabbitMessage rabbitMessage = new RabbitMessage().setId(null).setRedisCache(Arrays.asList(RedisCache.labelValue.name())).setStatus(RabbitStatus.UPDATE.name());
        rabbitTemplate.convertAndSend("", RabbitQueuesConfig.USER_DATA_FLUSH, rabbitMessage);
        //记录日志
        adminLogService.recordOperationLog(JwtUtils.parseJWT(token), new Date(), "LE003", "A update operation was performed on table label by id = " + editLabelData.getId());
        return ResponseResult.getSuccessResult(null, "The label data is modified");
    }
}




