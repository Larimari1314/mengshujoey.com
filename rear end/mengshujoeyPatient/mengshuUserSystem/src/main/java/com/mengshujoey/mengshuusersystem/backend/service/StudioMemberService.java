package com.mengshujoey.mengshuusersystem.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mengCommon.backend.pojo.StudioMember;
import com.mengCommon.common.common.http.ResponseResult;

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

    ResponseResult<String> queryStudioMember();

}
