package com.mengshujoey.mengshuadminsystem.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mengCommon.backend.dao.EvaluationDetailInformationMapper;
import com.mengCommon.backend.dao.StudioMemberMapper;
import com.mengCommon.backend.form.AddStudioMember;
import com.mengCommon.backend.form.DefaultQueryForm;
import com.mengCommon.backend.form.EditStudioMember;
import com.mengCommon.backend.form.RabbitMessage;
import com.mengCommon.backend.pojo.StudioMember;
import com.mengCommon.common.common.http.RabbitStatus;
import com.mengCommon.common.common.http.RedisCache;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengCommon.common.common.http.StatusCode;
import com.mengCommon.common.config.RabbitQueuesConfig;
import com.mengCommon.common.utils.IdWorker;
import com.mengCommon.common.utils.JwtUtils;
import com.mengshujoey.mengshuadminsystem.backend.service.AdminLogService;
import com.mengshujoey.mengshuadminsystem.backend.service.StudioMemberService;
import com.mengshujoey.mengshuadminsystem.common.utils.ObsUtil;
import com.mengshujoey.mengshuadminsystem.common.utils.StringCapitalizationUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * application name:mengshujoeyPatient - StudioMemberServiceImpl
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【studio_member】的数据库操作Service实现
 * @createDate 2023-01-14 15:08:18
 * @since 1.8
 */
@Service
public class StudioMemberServiceImpl extends ServiceImpl<StudioMemberMapper, StudioMember>
        implements StudioMemberService {
    @Autowired
    private StudioMemberMapper studioMemberMapper;
    @Autowired
    private ObsUtil obsUtil;
    @Autowired
    private AdminLogService adminLogService;
    @Autowired
    private EvaluationDetailInformationMapper evaluationDetailInformationMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Override
    public ResponseResult<ArrayList<Map<String, Object>>> getStudioMember() {
        ArrayList<Map<String, Object>> studioMember = studioMemberMapper.getStudioMember();
        return ResponseResult.getSuccessResult(studioMember, "Get worker member information successfully");
    }

    @Override
    public ResponseResult<PageInfo<Map<String, Object>>> queryStudioData(DefaultQueryForm defaultQueryForm) {
        PageHelper.startPage(defaultQueryForm.getPage(), 5);
        ArrayList<Map<String, Object>> arrayList = studioMemberMapper.queryStudioData(defaultQueryForm);
        PageInfo<Map<String, Object>> studioData = new PageInfo<>(arrayList);
        return ResponseResult.getSuccessResult(studioData, "The query of studio member information was successful");
    }

    @Override
    public String uploadCoverAddress(MultipartFile memberAvatar) {
        long fileName = new IdWorker().nextId();
        try {
            return obsUtil.uploadFile(memberAvatar, "mengshujoey/evaluationImage/" + fileName + ".jpg");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public ResponseResult<String> addStudioMember(AddStudioMember studioMember, String token) {
        studioMember.setId(StringCapitalizationUtils.convertToLowercase(studioMember.getMemberName()));
        studioMemberMapper.addStudioMember(studioMember);
        adminLogService.recordOperationLog(JwtUtils.parseJWT(token), new Date(), "LE001", "An add operation was performed on table studio_member by id = " + studioMember.getId());
        //向rabbitMQ发送更新数据
        RabbitMessage rabbitMessage=new RabbitMessage().setId(null).setRedisCache(Arrays.asList(RedisCache.studioList.name())).setStatus(RabbitStatus.ADD.name());
        //发送数据
        rabbitTemplate.convertAndSend("", RabbitQueuesConfig.USER_DATA_FLUSH, rabbitMessage);
        return ResponseResult.getSuccessResult(null, "Add studio members successfully");
    }

    @Override
    public ResponseResult<String> deleteStudioMember(LinkedList<String> deleteList, String token) {
        Integer result = evaluationDetailInformationMapper.queryExists(deleteList);
        if(result!=0){
            return ResponseResult.getErrorResult("Delete the id that exists for the binding value", StatusCode.NOT_MODIFIED, null);
        }
        studioMemberMapper.deleteByIds(deleteList);
        //记录日志
        adminLogService.recordOperationLog(JwtUtils.parseJWT(token), new Date(), "LE002", "A delete operation was performed on table studio_member by id = " + StringUtils.join(deleteList, ','));
        //向rabbitMQ发送更新数据
        RabbitMessage rabbitMessage=new RabbitMessage().setId(null).setRedisCache(Arrays.asList(RedisCache.studioList.name())).setStatus(RabbitStatus.DELETE.name());
        //发送数据
        rabbitTemplate.convertAndSend("", RabbitQueuesConfig.USER_DATA_FLUSH, rabbitMessage);
        return ResponseResult.getSuccessResult(null, "Delete studio_member data was successful");
    }

    @Override
    public ResponseResult<String> editStudioMember(EditStudioMember studioMember, String token) {
        studioMemberMapper.editStudioMember(studioMember);
        //记录日志
        adminLogService.recordOperationLog(JwtUtils.parseJWT(token), new Date(), "LE003", "A update operation was performed on table studio_member by id = " + studioMember.getId());
        //向rabbitMQ发送更新数据
        RabbitMessage rabbitMessage=new RabbitMessage().setId(null).setRedisCache(Arrays.asList(RedisCache.studioList.name())).setStatus(RabbitStatus.UPDATE.name());
        //发送数据
        rabbitTemplate.convertAndSend("", RabbitQueuesConfig.USER_DATA_FLUSH, rabbitMessage);
        return ResponseResult.getSuccessResult(null, "The member information was modified successfully");
    }
}




