package com.mengshujoey.mengshuusersystem.common.sercurity.utils;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

/**
 * application name:mengshujoeyPatient - EncryptDataUtils
 * application describing:
 * copyright:
 * company:
 * time:2023-02-11 10:19:49
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
public class EncryptDataUtils {
    /**
     * 加密/解密算法名称
     */
    private static final String ALGORITHM = "AES";


    /**
     * 数据加密: 明文 -> 密文
     */
    public static String encrypt(String plainByte, String keys) throws Exception {
        String result = null;
        try {
            byte[] content = new byte[0];
            content = plainByte.getBytes(StandardCharsets.UTF_8);
            byte[] keyByte = keys.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(keyByte, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] data = cipher.doFinal(content);
            result = Base64.getEncoder().encodeToString(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 数据解密: 密文 -> 明文
     */
    public static String decrypt(String cipherByte, String keys) throws Exception {
        String result = null;
        try {
            byte[] messageByte = Base64.getDecoder().decode(cipherByte);
            byte[] keyByte = keys.getBytes(StandardCharsets.UTF_8);
            SecretKeySpec keySpec = new SecretKeySpec(keyByte, "AES");
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] content = cipher.doFinal(messageByte);
            result = new String(content, StandardCharsets.UTF_8);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
