package com.mengshujoey.mengshuadminsystem.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mengCommon.backend.pojo.ConstantItem;
import com.mengCommon.common.common.http.ResponseResult;

import java.util.ArrayList;
import java.util.Map;

/**
 * application name:mengshujoeyPatient - ConstantItemService
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【constant_item】的数据库操作Service
 * @createDate 2023-01-14 15:07:55
 * @since 1.8
 */
public interface ConstantItemService extends IService<ConstantItem> {

    ResponseResult<ArrayList<Map<String, Object>>> getReferralLevel();

    ResponseResult<ArrayList<Map<String, Object>>> getSexList();

}
