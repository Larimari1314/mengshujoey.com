package com.mengshujoey.mengshuadminsystem.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mengCommon.backend.dao.RoleInfoMapper;
import com.mengCommon.backend.dao.RoleModuleMapper;
import com.mengCommon.backend.form.AddRoleInfo;
import com.mengCommon.backend.form.DefaultQueryForm;
import com.mengCommon.backend.form.EditRoleInfoForm;
import com.mengCommon.backend.form.QueryRoleData;
import com.mengCommon.backend.pojo.RoleInfo;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengCommon.common.utils.JwtUtils;
import com.mengshujoey.mengshuadminsystem.backend.service.AdminLogService;
import com.mengshujoey.mengshuadminsystem.backend.service.RoleInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;

/**
 * application name:mengshujoeyPatient - RoleInfoServiceImpl
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【role_info】的数据库操作Service实现
 * @createDate 2023-01-14 15:08:13
 * @since 1.8
 */
@Service
public class RoleInfoServiceImpl extends ServiceImpl<RoleInfoMapper, RoleInfo>
        implements RoleInfoService {

    @Autowired
    private RoleInfoMapper roleInfoMapper;
    @Autowired
    private RoleModuleMapper roleModuleMapper;
    @Autowired
    private AdminLogService adminLogService;

    @Override
    public ResponseResult<PageInfo<QueryRoleData>> findRoleList(DefaultQueryForm queryForm) {
        PageHelper.startPage(queryForm.getPage(), 4);
        ArrayList<QueryRoleData> roleList = roleInfoMapper.findRoleList(queryForm);
        PageInfo<QueryRoleData> pageInfo = new PageInfo<>(roleList);
        return ResponseResult.getSuccessResult(pageInfo, "The query list of roles succeeded");
    }

    @Transactional
    @Override
    public ResponseResult<String> addRoleByAdmin(AddRoleInfo roleInfo, String token) {
        //主键回填获取id
        roleInfoMapper.addRoleInfo(roleInfo);

        System.out.println(roleInfo.getId());

        //添加数据
        for (Integer moduleId : roleInfo.getModuleList()) {
            roleModuleMapper.addModuleByRoleId(moduleId, roleInfo.getId());
        }
        //记录日志
        adminLogService.recordOperationLog(JwtUtils.parseJWT(token), new Date(), "LE001", "An add operation was performed on table role_module by id = " + roleInfo.getId());
        return ResponseResult.getSuccessResult(null, "The role module item was added successfully");
    }

    @Transactional
    @Override
    public ResponseResult<String> editRoleById(EditRoleInfoForm editRoleInfoForm, String token) {
        //修改角色名名称
        roleInfoMapper.editRileInfo(editRoleInfoForm);
        //删除绑定值
        roleModuleMapper.deleteByRoleId(editRoleInfoForm.getId());
        for (Integer moduleId : editRoleInfoForm.getModuleList()) {
            //添加绑定值
            roleModuleMapper.addModuleByRoleId(moduleId, editRoleInfoForm.getId());
        }
        //添加日志
        adminLogService.recordOperationLog(JwtUtils.parseJWT(token), new Date(), "LE003", "A update operation was performed on table role_module、role_info by roleId = " + editRoleInfoForm.getId());
        return ResponseResult.getSuccessResult(null, "The role information was modified successfully");
    }

    @Override
    public ResponseResult<ArrayList<Map<String, Object>>> getRoleList() {
        ArrayList<Map<String, Object>> roleInfo = roleInfoMapper.getRoleInfo();
        return ResponseResult.getSuccessResult(roleInfo,"Get the sex roleInfo drop-down list successfully");
    }
}




