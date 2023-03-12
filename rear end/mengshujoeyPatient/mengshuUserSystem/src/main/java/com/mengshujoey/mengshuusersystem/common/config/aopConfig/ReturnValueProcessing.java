package com.mengshujoey.mengshuusersystem.common.config.aopConfig;

import com.mengCommon.common.common.http.ResponseResult;
import com.mengCommon.common.common.http.StatusCode;
import com.mengshujoey.mengshuusersystem.backend.service.impl.TimedTasksServiceImpl;
import com.mengshujoey.mengshuusersystem.common.sercurity.utils.EncryptDataUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

/**
 * application name:mengshujoeyPatient - ReturnValueProcessing
 * application describing:
 * copyright:
 * company:
 * time:2023-03-12 12:11:42
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@Aspect
@Component
public class ReturnValueProcessing {
    @Value("${redisCache.cacheEncryption}")
    private Boolean redisCache;
    @Autowired
    private RedisTemplate redisTemplate;

    @Around("@annotation(com.mengshujoey.mengshuusersystem.common.config.aopConfig.ReturnProcess)")
    public ResponseResult<String> returnValueProcess(ProceedingJoinPoint pjp) throws Throwable {
        //方案2
        Object result = pjp.proceed();
        ResponseResult<String> resultOver = (ResponseResult<String>) result;
        if(!redisCache && resultOver.getCode().equals("200")){
            String privateKey = (String) redisTemplate.boundHashOps(TimedTasksServiceImpl.REDIS_ENCRYPTED_DATA).get(TimedTasksServiceImpl.REDIS_KEY_STRING);
            if (privateKey == null) {
                //钥匙为空
                return ResponseResult.getErrorResult("The website is not currently initialized, please refresh the interface and try again", StatusCode.BAD_REQUEST, null);
            }
            String encryptData = null;
            try {
                encryptData = EncryptDataUtils.encrypt(resultOver.getData(), privateKey);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            resultOver.setData(encryptData);
        }
        return resultOver;
    }
}
