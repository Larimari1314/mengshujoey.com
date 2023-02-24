package com.mengshujoey.mengshuadminsystem.common.utils;

import com.mengCommon.common.config.HuaweiObsConfig;
import com.obs.services.ObsClient;
import com.obs.services.model.PutObjectResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@Component
public class ObsUtil {

    @Autowired
    private HuaweiObsConfig huaweiObs;

    @Value("${huawei.obs.bucketName}")
    private String bucketName;

    //上传文件，multipartFile就是你要的文件，
    //objectKey就是文件名，如果桶中有文件夹的话，如往test文件上传test.txt文件，那么objectKey就是test/test.txt
    public String uploadFile(MultipartFile multipartFile, String folderName) throws Exception {
        InputStream inputStream = multipartFile.getInputStream();
        ObsClient instance = huaweiObs.getInstance();
        PutObjectResult putObjectResult = instance.putObject(bucketName, folderName, inputStream);
        inputStream.close();
        instance.close();
        return putObjectResult.getObjectUrl();
    }

    public void deleteFile(String objectKey) throws Exception {
        ObsClient instance = huaweiObs.getInstance();
        instance.deleteObject(bucketName, objectKey);
        instance.close();
    }
}
