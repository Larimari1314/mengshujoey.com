package com.mengshujoey.mengshuadminsystem.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mengCommon.backend.form.AddAdminForm;
import com.mengCommon.backend.form.DefaultQueryForm;
import com.mengCommon.backend.form.EditAdminInfo;
import com.mengCommon.backend.form.EditAdminInformation;
import com.mengCommon.backend.pojo.AdminInformation;
import com.mengCommon.common.common.http.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * application name:mengshujoeyPatient - AdminInformationService
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【admin_information】的数据库操作Service
 * @createDate 2023-01-14 15:07:35
 * @since 1.8
 */
public interface AdminInformationService extends IService<AdminInformation> {

    ResponseResult<PageInfo<Map<String, Object>>> findAdminByName(DefaultQueryForm form);

    ResponseResult<String> editAdminInformation(EditAdminInfo adminInfo,String token);

    String uploadAvatarAddress(MultipartFile avatar);

    ResponseResult<String> addAdmin(AddAdminForm adminInfo, String token);

    ResponseResult<String> changeAdminStatus(Long id, Boolean status, String token);

    ResponseResult<String> editAdminInformationByIndividual(EditAdminInformation adminInformation);
}
