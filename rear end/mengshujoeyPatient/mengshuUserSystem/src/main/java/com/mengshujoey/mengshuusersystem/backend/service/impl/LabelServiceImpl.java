package com.mengshujoey.mengshuusersystem.backend.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengCommon.backend.dao.LabelMapper;
import com.mengCommon.backend.pojo.Label;
import com.mengCommon.common.common.http.RedisCache;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengCommon.common.common.http.StatusCode;
import com.mengshujoey.mengshuusersystem.backend.service.LabelService;
import com.mengshujoey.mengshuusersystem.common.sercurity.utils.EncryptDataUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundValueOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

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
    private ReentrantLock LABEL_LOCK=new ReentrantLock();
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private LabelMapper labelMapper;
    @Override
    public ResponseResult<String> getLabelValue() {
        //从redis中查找数据
        BoundValueOperations labelValue = redisTemplate.boundValueOps(RedisCache.labelValue.toString());
        Object labelList = labelValue.get();
        if(!ObjectUtils.isEmpty(labelList)){
            //说明数据存在
            return ResponseResult.getSuccessResult(String.valueOf(labelList), "Label data found successfully");
        }
        //标签数据为空
        //加锁
        LABEL_LOCK.lock();
        try {
            //再次查询防止重复添加
            Object labelListTwo = labelValue.get();
            if(!ObjectUtils.isEmpty(labelListTwo)){
                return ResponseResult.getSuccessResult(String.valueOf(labelListTwo), "Label data found successfully");
            }else{
                //不存在，查询数据库
                ArrayList<Map<String, Object>> labelArrayList = labelMapper.tabDropDown();
                //加密存储到redis中并返回
                String privateKey = (String) redisTemplate.boundHashOps(TimedTasksServiceImpl.REDIS_ENCRYPTED_DATA).get(TimedTasksServiceImpl.REDIS_KEY_STRING);
                if(privateKey==null){
                    //钥匙为空
                    return ResponseResult.getErrorResult("The website is not currently initialized, please refresh the interface and try again", StatusCode.BAD_REQUEST,null);
                }
                String encryptData = null;
                try {
                    encryptData = EncryptDataUtils.encrypt(JSON.toJSONString(labelArrayList),privateKey);
                    //将数据存储到redis中
                    labelValue.set(encryptData);
                } catch (Exception e) {
                    log.error("An error occurred in the encryption process");
                    throw new RuntimeException(e);
                }
                return ResponseResult.getSuccessResult(encryptData, "Label data found successfully");
            }
        }finally {
            LABEL_LOCK.unlock();
        }
    }
}




