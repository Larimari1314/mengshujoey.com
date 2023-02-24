package com.mengshujoey.mengshuusersystem.backend.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mengCommon.backend.dao.EvaluationBasicInformationMapper;
import com.mengCommon.backend.form.QueryDataByUser;
import com.mengCommon.backend.form.QueryProjectByUser;
import com.mengCommon.backend.pojo.EvaluationBasicInformation;
import com.mengCommon.common.common.http.RedisCache;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengCommon.common.common.http.StatusCode;
import com.mengshujoey.mengshuusersystem.backend.service.EvaluationBasicInformationService;
import com.mengshujoey.mengshuusersystem.common.sercurity.utils.EncryptDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.concurrent.locks.ReentrantLock;

/**
 * application name:mengshujoeyPatient - EvaluationBasicInformationServiceImpl
 * application describing:
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
public class EvaluationBasicInformationServiceImpl extends ServiceImpl<EvaluationBasicInformationMapper, EvaluationBasicInformation>
        implements EvaluationBasicInformationService {
    private static final ReentrantLock PROJECT_LOCK_SINGLE = new ReentrantLock();
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private EvaluationBasicInformationMapper evaluationBasicInformationMapper;

    @Override
    public ResponseResult<String> queryEvaluationBasic(String basicId) {
        if (basicId.length() > 20) {
            return ResponseResult.getErrorResult("Illegal data length", StatusCode.BAD_REQUEST, null);
        }
        //reis中查询信息
        BoundHashOperations projectSingle = redisTemplate.boundHashOps(RedisCache.projectSingle.toString());
        Object result = projectSingle.get(basicId);
        if (!ObjectUtils.isEmpty(result)) {
            //不为空，返回数据
            return ResponseResult.getSuccessResult((String) result, "The query item succeeded");
        }
        //不存在逻辑
        PROJECT_LOCK_SINGLE.lock();
        try {
            //查询是否存在
            Object resultFul = projectSingle.get(basicId);
            if (!ObjectUtils.isEmpty(resultFul)) {
                //不为空，返回数据
                return ResponseResult.getSuccessResult((String) resultFul, "The query item succeeded");
            }
            //不存在则查询
            QueryProjectByUser queryProjectByUser = evaluationBasicInformationMapper.queryEvaluationBasic(basicId);
            //判空
            if (ObjectUtils.isEmpty(queryProjectByUser)) {
                //为空
                return ResponseResult.getErrorResult("The resource queried does not exist", StatusCode.NOT_FOUND, null);
            }
            //将查询数据加密
            String privateKey = (String) redisTemplate.boundHashOps(TimedTasksServiceImpl.REDIS_ENCRYPTED_DATA).get(TimedTasksServiceImpl.REDIS_KEY_STRING);
            if (privateKey == null) {
                //钥匙为空
                return ResponseResult.getErrorResult("The website is not currently initialized, please refresh the interface and try again", StatusCode.BAD_REQUEST, null);
            }
            String encryptData = null;
            try {
                encryptData = EncryptDataUtils.encrypt(JSON.toJSONString(queryProjectByUser), privateKey);
                projectSingle.put(basicId, encryptData);
            } catch (Exception e) {
                log.error("An error occurred in the encryption process");
                throw new RuntimeException(e);
            }
            return ResponseResult.getSuccessResult(encryptData, "The data query succeeded");
        } finally {
            PROJECT_LOCK_SINGLE.unlock();
        }
    }

    @Override
    public ResponseResult<String> queryData(QueryDataByUser data) {
        //过滤请求
        if ((data.getData() != null && data.getData().length() > 20) || (data.getLevelId() != null && data.getLevelId() > 20)) {
            return ResponseResult.getErrorResult("The request data does not meet the specification", StatusCode.Requested_range_not_satisfiable, null);
        }
        PageHelper.startPage(data.getPage(), data.getPageSize());
        //查询数据喽，此部分数据不使用缓存
        ArrayList<QueryDataByUser> queryDataByUsers = evaluationBasicInformationMapper.queryDataByUser(data);
        PageInfo<QueryDataByUser> pageInfo = new PageInfo<>(queryDataByUsers);
        //返回数据
        String privateKey = (String) redisTemplate.boundHashOps(TimedTasksServiceImpl.REDIS_ENCRYPTED_DATA).get(TimedTasksServiceImpl.REDIS_KEY_STRING);
        if (privateKey == null) {
            //钥匙为空
            return ResponseResult.getErrorResult("The website is not currently initialized, please refresh the interface and try again", StatusCode.BAD_REQUEST, null);
        }
        String encryptData = null;
        try {
            encryptData = EncryptDataUtils.encrypt(JSON.toJSONString(pageInfo), privateKey);
        } catch (Exception e) {
            log.error("An error occurred in the encryption process");
            throw new RuntimeException(e);
        }
        return ResponseResult.getSuccessResult(encryptData, "The data query succeeded");
    }
}




