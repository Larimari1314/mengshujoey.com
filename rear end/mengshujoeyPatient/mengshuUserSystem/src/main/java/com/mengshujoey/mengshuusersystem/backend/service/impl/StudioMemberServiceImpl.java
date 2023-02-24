package com.mengshujoey.mengshuusersystem.backend.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengCommon.backend.dao.StudioMemberMapper;
import com.mengCommon.backend.pojo.StudioMember;
import com.mengCommon.common.common.http.RedisCache;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengCommon.common.common.http.StatusCode;
import com.mengshujoey.mengshuusersystem.backend.service.StudioMemberService;
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
    private RedisTemplate redisTemplate;
    @Autowired
    private StudioMemberMapper studioMemberMapper;

    private static final ReentrantLock STUDIO_LOCK=new ReentrantLock();

    @Override
    public ResponseResult<String> queryStudioMember() {
        //从redis中查询信息
        BoundValueOperations studioList = redisTemplate.boundValueOps(RedisCache.studioList.toString());
        Object studioMemberOneList = studioList.get();
        if(!ObjectUtils.isEmpty(studioMemberOneList)){
            //返回数据
            return ResponseResult.getSuccessResult(String.valueOf(studioMemberOneList), "The query of studio member information was successful");
        }else{
            //为空
            STUDIO_LOCK.lock();
            try {
                //查询是否存在
                Object studioMemberTwoList = studioList.get();
                if(!ObjectUtils.isEmpty(studioMemberTwoList)){
                    return ResponseResult.getSuccessResult(String.valueOf(studioMemberTwoList), "The query of studio member information was successful");
                }
                //查询数据
                ArrayList<Map<String, Object>> arrayList = studioMemberMapper.queryStudioList();
                //加密数据
                //加密存储到redis中并返回
                String privateKey = (String) redisTemplate.boundHashOps(TimedTasksServiceImpl.REDIS_ENCRYPTED_DATA).get(TimedTasksServiceImpl.REDIS_KEY_STRING);
                if(privateKey==null){
                    //钥匙为空
                    return ResponseResult.getErrorResult("The website is not currently initialized, please refresh the interface and try again", StatusCode.BAD_REQUEST,null);
                }
                String encryptData = null;
                try {
                    encryptData = EncryptDataUtils.encrypt(JSON.toJSONString(arrayList),privateKey);
                    //将数据存储到redis中
                    studioList.set(String.valueOf(encryptData));
                } catch (Exception e) {
                    log.error("An error occurred in the encryption process");
                    throw new RuntimeException(e);
                }
                return ResponseResult.getSuccessResult(encryptData, "The data query succeeded");
            }finally {
                STUDIO_LOCK.unlock();
            }
        }
    }
}




