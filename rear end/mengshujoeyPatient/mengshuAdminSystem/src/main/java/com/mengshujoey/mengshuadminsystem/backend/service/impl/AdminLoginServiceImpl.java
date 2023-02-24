package com.mengshujoey.mengshuadminsystem.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mengCommon.backend.dao.AdminLoginMapper;
import com.mengCommon.backend.form.DefaultSingleValue;
import com.mengCommon.backend.form.ManagerLoginInformation;
import com.mengCommon.backend.pojo.AdminLogin;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengCommon.common.common.http.StatusCode;
import com.mengCommon.common.config.RSAEncrypt;
import com.mengCommon.common.utils.JwtUtils;
import com.mengshujoey.mengshuadminsystem.backend.service.AdminLogService;
import com.mengshujoey.mengshuadminsystem.backend.service.AdminLoginService;
import com.mengshujoey.mengshuadminsystem.backend.service.TimedTasksService;
import com.mengshujoey.mengshuadminsystem.common.utils.SendMessageByEmail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * application name:mengshujoeyPatient - AdminLoginServiceImpl
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【admin_login】的数据库操作Service实现
 * @createDate 2023-01-14 15:07:52
 * @since 1.8
 */
@Service
public class AdminLoginServiceImpl extends ServiceImpl<AdminLoginMapper, AdminLogin>
        implements AdminLoginService {
    @Autowired
    private AdminLoginMapper adminLoginMapper;
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private TimedTasksService timedTasksService;
    @Autowired
    private AdminLogService adminLogService;
    @Autowired
    private SendMessageByEmail sendMessageByEmail;

    @Override
    public ManagerLoginInformation getAdminInformation(String loginEmail) {
        return adminLoginMapper.loginByEmail(loginEmail);
    }

    @Override
    public ResponseResult<String> obtainPublicKey() {
        //前端登录之前获取私钥地址
        //检测redis中是否存在公钥
        String publicKey = (String) redisTemplate.boundHashOps(TimedTasksServiceImpl.REDIS_ENCRYPTED_DATA).get(TimedTasksServiceImpl.REDIS_PUBLIC_KEY_STRING);
        if (publicKey == null) {
            timedTasksService.refreshEncryptedData();
            return this.obtainPublicKey();
        }
        //将私钥返回
        return ResponseResult.getSuccessResult(publicKey, "Please use this public key to encrypt your login information when you log in");
    }

    @Transactional
    @Override
    public ResponseResult<String> resetPassword(Long id, DefaultSingleValue defaultSingleValue, String token) {
        //重置密码，加密密码
        String newPassword = BCrypt.hashpw(defaultSingleValue.getValue(), BCrypt.gensalt());
        adminLoginMapper.updatePassword(id, newPassword);
        Map<String, Object> adminInformation = adminLoginMapper.queryAdminInformation(id);
        //存入日志
        adminLogService.recordOperationLog(JwtUtils.parseJWT(token), new Date(), "LE003", "A password reset was performed for users with ID = " + id);
        //通知用户
        sendMessageByEmail.resetPasswordSendMessage((String) adminInformation.get("name"), (String) adminInformation.get("avatar"), (String) adminInformation.get("email"));
        return ResponseResult.getSuccessResult(null, "The password reset was successful");
    }

    @Override
    public ResponseResult<String> modifyLoginInformation(Map<String, Object> loginInformation) {
        String privateKey;
        synchronized (this) {
            privateKey = (String) redisTemplate.boundHashOps(TimedTasksServiceImpl.REDIS_ENCRYPTED_DATA).get(TimedTasksServiceImpl.REDIS_PRIVATE_KEY_STRING);
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ConcurrentHashMap<String,Object> loginInformationMap=new ConcurrentHashMap<>();
        try {
            loginInformationMap =objectMapper.readValue(RSAEncrypt.decrypt((String) loginInformation.get("LoginCredentials"), privateKey), ConcurrentHashMap.class);
            if(adminLoginMapper.detectDuplicates((String) loginInformationMap.get("loginEmail"),(String) loginInformationMap.get("id"))!=0){
                return ResponseResult.getErrorResult("The current message is duplicated", StatusCode.NOT_MODIFIED,null);
            }
            adminLoginMapper.updateLogin((String)loginInformationMap.get("loginEmail"),BCrypt.hashpw(String.valueOf(loginInformationMap.get("password")), BCrypt.gensalt()), (String) loginInformationMap.get("id"));
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseResult.getErrorResult( "The login information was modified fail",StatusCode.ERROR,null);
        }
        return ResponseResult.getSuccessResult(null, "The login information was modified successfully");
    }

}