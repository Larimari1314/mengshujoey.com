package com.mengshujoey.mengshuadminsystem.backend.api;

import com.github.pagehelper.PageInfo;
import com.mengCommon.backend.form.AddStudioMember;
import com.mengCommon.backend.form.DefaultQueryForm;
import com.mengCommon.backend.form.EditStudioMember;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengshujoey.mengshuadminsystem.backend.service.StudioMemberService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.LinkedList;
import java.util.Map;

/**
 * application name:mengshujoeyPatient - StudioServlet
 * application describing: 工作室成员管理
 * copyright:
 * company:
 * time:2023-02-04 09:46:22
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@RestController
@RequestMapping("/studioManage")
@Valid
public class StudioServlet {
    @Autowired
    private StudioMemberService studioMemberService;

    /**
     * 查询工作室测评成员
     *
     * @param defaultQueryForm
     * @return
     */
    @ApiOperation(value = "查询工作测评成员")
    @PreAuthorize("hasAnyAuthority('6','19')")
    @PostMapping("/queryStudioData")
    public ResponseResult<PageInfo<Map<String, Object>>> queryLabelData(@RequestBody @Valid DefaultQueryForm defaultQueryForm) {
        return studioMemberService.queryStudioData(defaultQueryForm);
    }

    /**
     * 上传头像
     *
     * @param memberAvatar
     * @return
     */
    @ApiOperation(value = "上传头像")
    @PreAuthorize("hasAnyAuthority('6','20','22')")
    @PostMapping("/uploadMemberAvatarAddress")
    public String uploadCoverAddress(@RequestBody MultipartFile memberAvatar) {
        return studioMemberService.uploadCoverAddress(memberAvatar);
    }

    /**
     * 添加工作室成员
     *
     * @param studioMember
     * @return
     */
    @ApiOperation(value = "添加工作室成员")
    @PreAuthorize("hasAnyAuthority('6','20')")
    @PostMapping("/addStudioMember")
    public ResponseResult<String> addStudioMember(@RequestBody @Valid AddStudioMember studioMember, @RequestHeader(value = "token") String token) {
        return studioMemberService.addStudioMember(studioMember, token);
    }

    /**
     * 删除工作室成员
     *
     * @param deleteList
     * @param token
     * @return
     */
    @ApiOperation(value = "删除工作室成员")
    @PreAuthorize("hasAnyAuthority('6','21')")
    @PostMapping("/deleteStudioMember")
    public ResponseResult<String> deleteStudioMember(@RequestBody @Size(min = 1, message = "删除列表至少有一个") LinkedList<String> deleteList, @RequestHeader(value = "token") String token) {
        return studioMemberService.deleteStudioMember(deleteList, token);
    }

    /**
     * 修改工作室成员
     *
     * @param studioMember
     * @param token
     * @return
     */
    @ApiOperation(value = "修改工作室成员")
    @PreAuthorize("hasAnyAuthority('6','22')")
    @PostMapping("/editStudioMember")
    public ResponseResult<String> editStudioMember(@RequestBody EditStudioMember studioMember, @RequestHeader(value = "token") String token) {
        return studioMemberService.editStudioMember(studioMember, token);
    }
}
