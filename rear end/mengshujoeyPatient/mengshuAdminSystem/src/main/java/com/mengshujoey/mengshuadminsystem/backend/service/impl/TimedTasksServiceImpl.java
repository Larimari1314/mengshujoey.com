package com.mengshujoey.mengshuadminsystem.backend.service.impl;

import com.mengshujoey.mengshuadminsystem.backend.service.TimedTasksService;
import org.apache.commons.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

/**
 * application name:mengshujoeyPatient - TimedTasksServiceImpl
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14 17:16:26
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@Service
public class TimedTasksServiceImpl implements TimedTasksService {

    public final static String REDIS_ENCRYPTED_DATA = "encryptedData";
    public final static String REDIS_PRIVATE_KEY_STRING = "privateKeyString";
    public final static String REDIS_PUBLIC_KEY_STRING = "publicKeyString";
    @Autowired
    private RedisTemplate redisTemplate;

    /**
     * 生成公私钥存储到redis中
     */
    @Override
    public synchronized void refreshEncryptedData() {
        //生成公私钥
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = null;
        try {
            keyPairGen = KeyPairGenerator.getInstance("RSA");
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();   // 得到私钥
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();  // 得到公钥
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        // 得到私钥字符串
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        BoundHashOperations encryptedData = redisTemplate.boundHashOps(TimedTasksServiceImpl.REDIS_ENCRYPTED_DATA);
        encryptedData.put(TimedTasksServiceImpl.REDIS_PRIVATE_KEY_STRING, privateKeyString);
        encryptedData.put(TimedTasksServiceImpl.REDIS_PUBLIC_KEY_STRING, publicKeyString);
    }
}
