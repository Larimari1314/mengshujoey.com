package com.mengshujoey.mengshuadminsystem.backend.api;

import com.github.pagehelper.PageInfo;
import com.mengCommon.backend.form.AddAdminForm;
import com.mengCommon.backend.form.DefaultQueryForm;
import com.mengCommon.backend.form.DefaultSingleValue;
import com.mengCommon.backend.form.EditAdminInfo;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengshujoey.mengshuadminsystem.backend.service.AdminInformationService;
import com.mengshujoey.mengshuadminsystem.backend.service.AdminLoginService;
import com.mengshujoey.mengshuadminsystem.backend.service.RoleInfoService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Map;

/**
 * application name:mengshujoeyPatient - AdminManagerServlet
 * application describing: 管理员管理api
 * copyright:
 * company:
 * time:2023-02-06 14:12:24
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@RestController
@RequestMapping("/adminManager")
public class AdminManagerServlet {
    @Autowired
    private AdminInformationService adminInformationService;
    @Autowired
    private RoleInfoService roleInfoService;
    @Autowired
    private AdminLoginService adminLoginService;

    /**
     * 获取全部数据
     *
     * @param form
     * @return
     */
    @ApiOperation(value = "获取全部数据")
    @PreAuthorize("hasAnyAuthority('7','23')")
    @PostMapping("/findAdminByName")
    public ResponseResult<PageInfo<Map<String, Object>>> findAdminByName(@RequestBody @Valid DefaultQueryForm form) {
        return adminInformationService.findAdminByName(form);
    }

    /**
     * 获取角色下拉列表
     *
     * @return
     */
    @ApiOperation(value = "获取角色下拉列表")
    @PreAuthorize("hasAnyAuthority('7','23','24','26')")
    @GetMapping("/getRoleList")
    public ResponseResult<ArrayList<Map<String, Object>>> getRoleList() {
        return roleInfoService.getRoleList();
    }

    /**
     * 修改管理员信息
     *
     * @param adminInfo
     * @return
     */
    @ApiOperation(value = "修改管理员信息")
    @PreAuthorize("hasAnyAuthority('7','26')")
    @PostMapping("/editAdminInformation")
    public ResponseResult<String> editAdminInformation(@RequestBody @Valid EditAdminInfo adminInfo, @RequestHeader(value = "token") String token) {
        return adminInformationService.editAdminInformation(adminInfo, token);
    }

    /**
     * 上传用户头像
     *
     * @param avatar
     * @return
     */
    @ApiOperation(value = "上传用户头像")
    @PreAuthorize("hasAnyAuthority('7','24','26','10')")
    @PostMapping("/uploadAvatarAddress")
    public String uploadAvatarAddress(@RequestBody MultipartFile avatar) {
        return adminInformationService.uploadAvatarAddress(avatar);
    }

    /**
     * 重置密码
     *
     * @param id
     * @param defaultSingleValue
     * @param token
     * @return
     */
    @ApiOperation(value = "重置密码")
    @PreAuthorize("hasAnyAuthority('7','26')")
    @PostMapping("/resetPassword/{id}")
    public ResponseResult<String> resetPassword(@PathVariable Long id, @RequestBody @Valid DefaultSingleValue defaultSingleValue, @RequestHeader(value = "token") String token) {
        return adminLoginService.resetPassword(id, defaultSingleValue, token);
    }

    /**
     * 添加管理员
     *
     * @param adminInfo
     * @param token
     * @return
     */
    @ApiOperation(value = "添加管理员")
    @PreAuthorize("hasAnyAuthority('7','24')")
    @PostMapping("/addAdmin")
    public ResponseResult<String> addAdmin(@RequestBody @Valid AddAdminForm adminInfo, @RequestHeader(value = "token") String token) {
        return adminInformationService.addAdmin(adminInfo, token);
    }

    /**
     * 修改账号激活状态
     *
     * @param id
     * @param status
     * @param token
     * @return
     */
    @ApiOperation(value = "账号激活状态")
    @PreAuthorize("hasAnyAuthority('7','25')")
    @GetMapping("/changeAdminStatus/{id}")
    public ResponseResult<String> changeAdminStatus(@PathVariable Long id, @RequestParam("status") Boolean status, @RequestHeader(value = "token") String token) {
        return adminInformationService.changeAdminStatus(id, status, token);
    }
}
