package com.mengshujoey.mengshuadminsystem.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengCommon.backend.dao.EvaluationDetailInformationMapper;
import com.mengCommon.backend.form.AddDetailInformation;
import com.mengCommon.backend.form.EditDetailInformation;
import com.mengCommon.backend.form.RabbitMessage;
import com.mengCommon.backend.pojo.EvaluationDetailInformation;
import com.mengCommon.common.common.http.RabbitStatus;
import com.mengCommon.common.common.http.RedisCache;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengCommon.common.common.http.StatusCode;
import com.mengCommon.common.config.RabbitQueuesConfig;
import com.mengCommon.common.utils.JwtUtils;
import com.mengshujoey.mengshuadminsystem.backend.service.AdminLogService;
import com.mengshujoey.mengshuadminsystem.backend.service.EvaluationDetailInformationService;
import com.mengshujoey.mengshuadminsystem.common.utils.ObjectUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.concurrent.locks.StampedLock;

/**
 * application name:mengshujoeyPatient - EvaluationDetailInformationServiceImpl
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【evaluation_detail_information】的数据库操作Service实现
 * @createDate 2023-01-14 15:08:01
 * @since 1.8
 */
@Service
public class EvaluationDetailInformationServiceImpl extends ServiceImpl<EvaluationDetailInformationMapper, EvaluationDetailInformation>
        implements EvaluationDetailInformationService {
    @Autowired
    private EvaluationDetailInformationMapper evaluationDetailInformationMapper;
    @Autowired
    private ObjectUtils objectUtils;
    @Autowired
    private AdminLogService adminLogService;
    @Autowired
    private RabbitTemplate rabbitTemplate;

    @Transactional
    @Override
    public ResponseResult<String> editDetailInformation(EditDetailInformation editDetailInformation, String token) {
        StampedLock stampedLock = new StampedLock();
        Map<String, String> fraction = new HashMap<>();
        //判断当前数据
        if (!editDetailInformation.getAssessmentMode()) {
            //团队测评
            if (editDetailInformation.getFraction() == null || editDetailInformation.getFraction().size() < 1 || editDetailInformation.getScore() == null) {
                return ResponseResult.getErrorResult("Team project scores have at least one item", StatusCode.NOT_IMPLEMENTED, null);
            }
            editDetailInformation.setFraction((LinkedList<String>) objectUtils.fractionBySeven(editDetailInformation.getFraction()));
            //固定分数为七位
            //因为hashMap为线程非安全类，所以使用写锁将其锁住，因为存在空值所以无法使用ConcurrentHashMap
            long stomp = stampedLock.writeLock();
            try {
                for (int i = 0; i < 7; i++) {
                    fraction.put(ObjectUtils.fraction_name.get(i), editDetailInformation.getFraction().get(i));
                }
            } finally {
                stampedLock.unlockWrite(stomp);
            }
        }
        //添加数据
        evaluationDetailInformationMapper.editDetailProject(editDetailInformation, fraction);
        //记录日志
        adminLogService.recordOperationLog(JwtUtils.parseJWT(token), new Date(), "LE003", "A update operation was performed on table evaluation_detail_information by id = " + editDetailInformation.getId());
        //向rabbitMQ发送更新数据
        RabbitMessage rabbitMessage = new RabbitMessage().setId(null).setRedisCache(Arrays.asList(RedisCache.projectSingle.name())).setStatus(RabbitStatus.UPDATE.name());
        //发送数据
        rabbitTemplate.convertAndSend("", RabbitQueuesConfig.USER_DATA_FLUSH, rabbitMessage);
        return ResponseResult.getSuccessResult(null, "The basic data was modified successfully");
    }

    @Transactional
    @Override
    public ResponseResult<String> deleteDetailInformationById(Integer id, String token) {
        //根据id删除数据
        evaluationDetailInformationMapper.deleteProjectById(id);
        //记录日志
        adminLogService.recordOperationLog(JwtUtils.parseJWT(token), new Date(), "LE002", "A delete operation was performed on table evaluation_detail_information by id=" + id);
        //发送详细信息
        //向rabbitMQ发送更新数据
        RabbitMessage rabbitMessage = new RabbitMessage().setId(null).setRedisCache(Arrays.asList(RedisCache.projectSingle.name())).setStatus(RabbitStatus.DELETE.name());
        //发送数据
        rabbitTemplate.convertAndSend("", RabbitQueuesConfig.USER_DATA_FLUSH, rabbitMessage);
        return ResponseResult.getSuccessResult(null, "The detail data was delete successfully");
    }

    @Transactional
    @Override
    public ResponseResult<String> addDetailInformation(AddDetailInformation addDetailInformation, String token) {
        if (!addDetailInformation.getAssessmentMode()) {
            //团队测评
            if (addDetailInformation.getDetailInformation() == null || (addDetailInformation.getFraction() == null || addDetailInformation.getFraction().size() < 1)) {
                return ResponseResult.getErrorResult("Team assessment details or score lists are anomalous", StatusCode.NOT_IMPLEMENTED, null);
            }
            //通过，扩容
            addDetailInformation.setFraction((LinkedList<String>) objectUtils.fractionBySeven(addDetailInformation.getFraction()));
        }
        evaluationDetailInformationMapper.addDetailInformation(addDetailInformation);
        //记录日志
        adminLogService.recordOperationLog(JwtUtils.parseJWT(token), new Date(), "LE001", "An add operation was performed on table evaluation_detail_information");
        //向rabbitMQ发送更新数据
        RabbitMessage rabbitMessage = new RabbitMessage().setId(Arrays.asList(addDetailInformation.getProjectId())).setRedisCache(Arrays.asList(RedisCache.projectSingle.name())).setStatus(RabbitStatus.ADD.name());
        //发送数据
        rabbitTemplate.convertAndSend("", RabbitQueuesConfig.USER_DATA_FLUSH, rabbitMessage);
        return ResponseResult.getSuccessResult(null, "The item was added successfully");
    }
}




