package com.mengshujoey.mengshuadminsystem.backend.api;

import com.github.pagehelper.PageInfo;
import com.mengCommon.backend.form.*;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengshujoey.mengshuadminsystem.backend.service.ModuleInfoService;
import com.mengshujoey.mengshuadminsystem.backend.service.RoleInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;

/**
 * application name:mengshujoeyPatient - RoleInfoServlet
 * application describing: 角色管理
 * copyright:
 * company:
 * time:2023-02-04 18:08:42
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@RestController
@RequestMapping("/roleManager")
public class RoleInfoServlet {
    @Autowired
    private ModuleInfoService moduleInfoService;

    @Autowired
    private RoleInfoService roleInfoService;

    /**
     * 获取权限列表
     *
     * @return
     */
    @ApiOperation(value = "获取权限列表")
    @GetMapping("/findModuleInfo")
    @PreAuthorize("hasAnyAuthority('2')")
    public ResponseResult<ArrayList<ModuleQueryData>> findModuleInfo() {
        return moduleInfoService.findModuleInfo();
    }

    /**
     * 查询角色列表
     *
     * @param queryForm
     * @return
     */
    @ApiOperation(value = "查询角色列表")
    @PreAuthorize("hasAnyAuthority('2')")
    @PostMapping("/findRoleList")
    public ResponseResult<PageInfo<QueryRoleData>> findRoleList(@RequestBody @Valid DefaultQueryForm queryForm) {
        return roleInfoService.findRoleList(queryForm);
    }

    /**
     * 添加角色数据
     *
     * @param roleInfo
     * @return
     */
    @ApiOperation(value = "添加角色数据")
    @PreAuthorize("hasAnyAuthority('2')")
    @PostMapping("/addRole")
    public ResponseResult<String> addRoleByAdmin(@RequestBody @Valid AddRoleInfo roleInfo, @RequestHeader(value = "token") String token) {
        return roleInfoService.addRoleByAdmin(roleInfo, token);
    }

    /**
     * 修改角色信息
     *
     * @param editRoleInfoForm
     * @param token
     * @return
     */
    @ApiOperation(value = "修改角色信息")
    @PreAuthorize("hasAnyAuthority('2')")
    @PostMapping("/editRole")
    public ResponseResult<String> editRoleById(@RequestBody @Valid EditRoleInfoForm editRoleInfoForm, @RequestHeader(value = "token") String token) {
        return roleInfoService.editRoleById(editRoleInfoForm, token);
    }
}
