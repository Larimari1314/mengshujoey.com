package com.mengshujoey.mengshuusersystem.backend.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mengCommon.backend.dao.EvaluationBasicInformationMapper;
import com.mengCommon.backend.dao.EvaluationDetailInformationMapper;
import com.mengCommon.backend.form.QueryEvaluationDetailByUser;
import com.mengCommon.backend.pojo.EvaluationDetailInformation;
import com.mengCommon.common.common.http.RedisCache;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengCommon.common.common.http.StatusCode;
import com.mengCommon.common.utils.IdWorker;
import com.mengshujoey.mengshuusersystem.backend.service.EvaluationDetailInformationService;
import com.mengshujoey.mengshuusersystem.common.sercurity.utils.EncryptDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.locks.ReentrantLock;

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
    private static final ReentrantLock REENTRANT_LOCK_GETMILKTEA = new ReentrantLock();
    private static final ReentrantLock REENTRANT_LOCK_MILKUODATECODE = new ReentrantLock();
    private static final ReentrantLock REENTRANT_LOCK_MAIN_EVALUATION_BASIC = new ReentrantLock();
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private EvaluationBasicInformationMapper evaluationBasicInformationMapper;

    @Override
    public ResponseResult<String> getMilkTeaEvaluationData(QueryEvaluationDetailByUser evaluationDetail) {
        if (evaluationDetail.getPage() > 50) {
            //目前来说50往上页数相当于拍摄了250个视频 不太可能 pass
            return ResponseResult.getErrorResult("The number of pages exceeds the limit", StatusCode.Not_Acceptable, null);
        }
        //先从redis中查询信息
        BoundHashOperations mainEvaluationDetail = redisTemplate.boundHashOps(RedisCache.MainEvaluationDetail.toString());
        Object result = mainEvaluationDetail.get(String.valueOf(evaluationDetail.getPage()));
        if (result != null && !Objects.equals(result.toString(), "")) {
            return ResponseResult.getSuccessResult(result.toString(), "The data query succeeded");
        } else {
            REENTRANT_LOCK_GETMILKTEA.lock();
            try {
                //查询是否存在
                Object checkPlagiarism = mainEvaluationDetail.get(String.valueOf(evaluationDetail.getPage()));
                if (checkPlagiarism != null && !Objects.equals(checkPlagiarism.toString(), "")) {
                    return ResponseResult.getSuccessResult(checkPlagiarism.toString(), "The data query succeeded");
                } else {
                    //查询数据
                    PageHelper.startPage( evaluationDetail.getPage(),4);
                    ArrayList<Map<String, Object>> milkTeaEvaluationData = evaluationBasicInformationMapper.getMilkTeaEvaluationData();
                    if (milkTeaEvaluationData != null && milkTeaEvaluationData.size() != 0) {
                        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(milkTeaEvaluationData);
                        //加密存储到redis中并返回
                        String privateKey = (String) redisTemplate.boundHashOps(TimedTasksServiceImpl.REDIS_ENCRYPTED_DATA).get(TimedTasksServiceImpl.REDIS_KEY_STRING);
                        if(privateKey==null){
                            //钥匙为空
                            return ResponseResult.getErrorResult("The website is not currently initialized, please refresh the interface and try again",StatusCode.BAD_REQUEST,null);
                        }
                        String encryptData = null;
                        try {
                            encryptData = EncryptDataUtils.encrypt(JSON.toJSONString(pageInfo),privateKey);
                            //System.out.println(EncryptDataUtils.decryptByPrivateSublevelKey((String) redisTemplate.boundHashOps(TimedTasksServiceImpl.REDIS_ENCRYPTED_DATA).get(TimedTasksServiceImpl.REDIS_PRIVATE_KEY_STRING), encryptData));
                            //将数据存储到redis中
                            mainEvaluationDetail.put(String.valueOf(evaluationDetail.getPage()), encryptData);
                        } catch (Exception e) {
                            log.error("An error occurred in the encryption process");
                            throw new RuntimeException(e);
                        }
                        return ResponseResult.getSuccessResult(encryptData, "The data query succeeded");
                    } else {
                        //说明不存在数据
                        return ResponseResult.getErrorResult("The number of pages exceeds the limit", StatusCode.Not_Acceptable, null);
                    }
                }
            } finally {
                REENTRANT_LOCK_GETMILKTEA.unlock();
            }
        }
    }

    @Override
    public ResponseResult<String> milkTeaUpdateCode() {
        //从redis中获取更新码
        BoundValueOperations mainMickTeaCode1 = redisTemplate.boundValueOps(RedisCache.mainMickTeaCode.toString());
        Object mainMickTeaCode = mainMickTeaCode1.get();
        //判空
        if(!ObjectUtils.isEmpty(mainMickTeaCode)){
            return ResponseResult.getSuccessResult(String.valueOf(mainMickTeaCode), "Get the milk tea update code successfully");
        }else{
            //如果redis中不存在更新码
            REENTRANT_LOCK_MILKUODATECODE.lock();
            try {
                //检测当前是否存在
                Object mainMickTeaCodeTwo = mainMickTeaCode1.get();
                //判空
                if(!ObjectUtils.isEmpty(mainMickTeaCodeTwo)){
                    return ResponseResult.getSuccessResult(String.valueOf(mainMickTeaCodeTwo), "Get the milk tea update code successfully");
                }else{
                    //当前数据不存在
                    String updateId = String.valueOf(new IdWorker().nextId());
                    //向redis中存入更新码
                    mainMickTeaCode1.set(updateId);
                    //返回更新码
                    return ResponseResult.getSuccessResult(updateId, "Get the milk tea update code successfully");
                }
            }finally {
                //解锁
                REENTRANT_LOCK_MILKUODATECODE.unlock();
            }
        }
    }

    @Override
    public ResponseResult<String> findAllVideo(QueryEvaluationDetailByUser evaluationDetail) {
        if(evaluationDetail.getPage()>100){
            //100--400条测评视频基本不可能
            return ResponseResult.getErrorResult("The number of pages exceeds the limit", StatusCode.Not_Acceptable, null);
        }
        //从redis中查询信息
        BoundHashOperations mainEvaluationDetail = redisTemplate.boundHashOps(RedisCache.evaluationBasic.toString());
        Object result = mainEvaluationDetail.get(String.valueOf(evaluationDetail.getPage()));
        if (result != null && !Objects.equals(result.toString(), "")) {
            return ResponseResult.getSuccessResult(result.toString(), "The data query succeeded");
        } else {
            REENTRANT_LOCK_MAIN_EVALUATION_BASIC.lock();
            try {
                Object resultRepeat = mainEvaluationDetail.get(String.valueOf(evaluationDetail.getPage()));
                if (resultRepeat != null && !Objects.equals(resultRepeat.toString(), "")) {
                    return ResponseResult.getSuccessResult(resultRepeat.toString(), "The data query succeeded");
                }else{
                    PageHelper.startPage(evaluationDetail.getPage(), 4);
                    ArrayList<Map<String, Object>> evaluationData =evaluationBasicInformationMapper.findAllVideo();
                    if (evaluationData != null && evaluationData.size() != 0) {
                        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(evaluationData);
                        //加密存储到redis中并返回
                        String privateKey = (String) redisTemplate.boundHashOps(TimedTasksServiceImpl.REDIS_ENCRYPTED_DATA).get(TimedTasksServiceImpl.REDIS_KEY_STRING);
                        if(privateKey==null){
                            //钥匙为空
                            return ResponseResult.getErrorResult("The website is not currently initialized, please refresh the interface and try again",StatusCode.BAD_REQUEST,null);
                        }
                        String encryptData = null;
                        try {
                            encryptData = EncryptDataUtils.encrypt(JSON.toJSONString(pageInfo),privateKey);
                            //System.out.println(EncryptDataUtils.decryptByPrivateSublevelKey((String) redisTemplate.boundHashOps(TimedTasksServiceImpl.REDIS_ENCRYPTED_DATA).get(TimedTasksServiceImpl.REDIS_PRIVATE_KEY_STRING), encryptData));
                            //将数据存储到redis中
                            mainEvaluationDetail.put(String.valueOf(evaluationDetail.getPage()), encryptData);
                        } catch (Exception e) {
                            log.error("An error occurred in the encryption process");
                            throw new RuntimeException(e);
                        }
                        return ResponseResult.getSuccessResult(encryptData, "The data query succeeded");

                    } else {
                        //说明不存在数据
                        return ResponseResult.getErrorResult("The number of pages exceeds the limit", StatusCode.Not_Acceptable, null);
                    }
                }
            }finally {
                REENTRANT_LOCK_MAIN_EVALUATION_BASIC.unlock();
            }
        }
    }
    @Override
    public ResponseResult<String> evaluationVideoUpdateCode() {
        //从redis中获取更新码
        BoundValueOperations mainMickTeaCode1 = redisTemplate.boundValueOps(RedisCache.evaluationVideoCode.toString());
        Object mainMickTeaCode = mainMickTeaCode1.get();
        //判空
        if(!ObjectUtils.isEmpty(mainMickTeaCode)){
            return ResponseResult.getSuccessResult(String.valueOf(mainMickTeaCode), "Get the evaluation video code update code successfully");
        }else{
            //如果redis中不存在更新码
            REENTRANT_LOCK_MILKUODATECODE.lock();
            try {
                //检测当前是否存在
                Object mainMickTeaCodeTwo = mainMickTeaCode1.get();
                //判空
                if(!ObjectUtils.isEmpty(mainMickTeaCodeTwo)){
                    return ResponseResult.getSuccessResult(String.valueOf(mainMickTeaCodeTwo), "Get the evaluation video code update code successfully");
                }else{
                    //当前数据不存在
                    String updateId = String.valueOf(new IdWorker().nextId());
                    //向redis中存入更新码
                    mainMickTeaCode1.set(updateId);
                    //返回更新码
                    return ResponseResult.getSuccessResult(updateId, "Get the evaluation video code update code successfully");
                }
            }finally {
                //解锁
                REENTRANT_LOCK_MILKUODATECODE.unlock();
            }
        }
    }

}




