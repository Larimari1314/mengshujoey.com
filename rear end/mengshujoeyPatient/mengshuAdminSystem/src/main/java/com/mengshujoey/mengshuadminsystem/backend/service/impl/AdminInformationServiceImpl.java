package com.mengshujoey.mengshuadminsystem.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mengCommon.backend.dao.AdminInformationMapper;
import com.mengCommon.backend.dao.AdminLoginMapper;
import com.mengCommon.backend.form.AddAdminForm;
import com.mengCommon.backend.form.DefaultQueryForm;
import com.mengCommon.backend.form.EditAdminInfo;
import com.mengCommon.backend.form.EditAdminInformation;
import com.mengCommon.backend.pojo.AdminInformation;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengCommon.common.common.http.StatusCode;
import com.mengCommon.common.utils.IdWorker;
import com.mengCommon.common.utils.JwtUtils;
import com.mengshujoey.mengshuadminsystem.backend.service.AdminInformationService;
import com.mengshujoey.mengshuadminsystem.backend.service.AdminLogService;
import com.mengshujoey.mengshuadminsystem.common.utils.ObsUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * application name:mengshujoeyPatient - AdminInformationServiceImpl
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【admin_information】的数据库操作Service实现
 * @createDate 2023-01-14 15:07:35
 * @since 1.8
 */
@Service
public class AdminInformationServiceImpl extends ServiceImpl<AdminInformationMapper, AdminInformation>
        implements AdminInformationService {
    @Autowired
    private AdminInformationMapper adminInformationMapper;
    @Autowired
    private AdminLoginMapper adminLoginMapper;
    @Autowired
    private AdminLogService adminLogService;
    @Autowired
    private ObsUtil obsUtil;

    @Override
    public ResponseResult<PageInfo<Map<String, Object>>> findAdminByName(DefaultQueryForm form) {
        PageHelper.startPage(form.getPage(), 10);
        ArrayList<Map<String, Object>> adminInformation = adminInformationMapper.findAdminByName(form);
        PageInfo<Map<String, Object>> pageInfo = new PageInfo<>(adminInformation);
        return ResponseResult.getSuccessResult(pageInfo, "The query for administrator information was successful");
    }

    @Transactional
    @Override
    public ResponseResult<String> editAdminInformation(EditAdminInfo adminInfo, String token) {
        adminInfo.setAge(new Date().getYear() - adminInfo.getBirthday().getYear());
        //管理员信息修改
        adminInformationMapper.editAdminInformation(adminInfo);
        //管理员权限修改
        adminLoginMapper.editAdminPermissions(adminInfo);
        //记录日志
        adminLogService.recordOperationLog(JwtUtils.parseJWT(token), new Date(), "LE003", "A update operation was performed on table admin_login by id = " + adminInfo.getId());
        return ResponseResult.getSuccessResult(null, "A update admin information success");
    }

    @Override
    public String uploadAvatarAddress(MultipartFile avatar) {
        long fileName = new IdWorker().nextId();
        try {
            return obsUtil.uploadFile(avatar, "mengshujoey/adminAvatar/" + fileName + ".jpg");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    @Override
    public ResponseResult<String> addAdmin(AddAdminForm adminInfo, String token) {
        //为用户生成id
        adminInfo.setId(new IdWorker().nextId());
        adminInfo.setAge(new Date().getYear() - adminInfo.getBirthday().getYear());
        //检测当前登录邮件是否重复
        if(adminLoginMapper.detectDuplicates(adminInfo.getEmail(),null)!=0){
            return ResponseResult.getErrorResult("The current message is duplicated", StatusCode.NOT_MODIFIED,null);
        }
        //密码加密
        adminInfo.setPassword(new BCryptPasswordEncoder().encode(adminInfo.getPassword()));
        adminInformationMapper.addAdmin(adminInfo);
        adminLoginMapper.addAdmin(adminInfo);
        //记录日志
        adminLogService.recordOperationLog(JwtUtils.parseJWT(token), new Date(), "LE001", "Add administrator information, modify tables admin_login and admin_information, and add administrator ID to " + adminInfo.getId());

        return ResponseResult.getSuccessResult(null, "The administrator information was added successfully");
    }

    @Transactional
    @Override
    public ResponseResult<String> changeAdminStatus(Long id, Boolean status, String token) {
        adminInformationMapper.editAdminStatus(id,status);
        //记录日志
        adminLogService.recordOperationLog(JwtUtils.parseJWT(token), new Date(), "LE003", "A update operation was performed on table admin_login by id = " + id);
        return ResponseResult.getSuccessResult(null, "update status successful");
    }

    @Override
    public ResponseResult<String> editAdminInformationByIndividual(EditAdminInformation adminInformation) {
        adminInformation.setAge(new Date().getYear()-adminInformation.getBirthday().getYear());
        adminInformationMapper.editAdminInformationByIndividual(adminInformation);
        return ResponseResult.getSuccessResult(null, "The personal information is modified");
    }
}




