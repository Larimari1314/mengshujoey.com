package com.mengshujoey.mengshuadminsystem.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.github.pagehelper.PageInfo;
import com.mengCommon.backend.form.AddStudioMember;
import com.mengCommon.backend.form.DefaultQueryForm;
import com.mengCommon.backend.form.EditStudioMember;
import com.mengCommon.backend.pojo.StudioMember;
import com.mengCommon.common.common.http.ResponseResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Map;

/**
 * application name:mengshujoeyPatient - StudioMemberService
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【studio_member】的数据库操作Service
 * @createDate 2023-01-14 15:08:18
 * @since 1.8
 */
public interface StudioMemberService extends IService<StudioMember> {

    ResponseResult<ArrayList<Map<String, Object>>> getStudioMember();

    ResponseResult<PageInfo<Map<String, Object>>> queryStudioData(DefaultQueryForm defaultQueryForm);

    String uploadCoverAddress(MultipartFile memberAvatar);

    ResponseResult<String> addStudioMember(AddStudioMember studioMember,String token);

    ResponseResult<String> deleteStudioMember(LinkedList<String> deleteList, String token);

    ResponseResult<String> editStudioMember(EditStudioMember studioMember, String token);
}
