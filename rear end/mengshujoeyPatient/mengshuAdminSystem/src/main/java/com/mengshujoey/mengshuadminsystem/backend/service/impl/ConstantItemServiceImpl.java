package com.mengshujoey.mengshuadminsystem.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengCommon.backend.dao.ConstantItemMapper;
import com.mengCommon.backend.pojo.ConstantItem;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengshujoey.mengshuadminsystem.backend.service.ConstantItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

/**
 * application name:mengshujoeyPatient - ConstantItemServiceImpl
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【constant_item】的数据库操作Service实现
 * @createDate 2023-01-14 15:07:55
 * @since 1.8
 */
@Service
public class ConstantItemServiceImpl extends ServiceImpl<ConstantItemMapper, ConstantItem>
        implements ConstantItemService {
    @Autowired
    private ConstantItemMapper constantItemMapper;
    @Override
    public ResponseResult<ArrayList<Map<String, Object>>> getReferralLevel() {
        ArrayList<Map<String, Object>> constantInformation=constantItemMapper.getReferralLevel();
        return ResponseResult.getSuccessResult(constantInformation,"Get the Recommended Level drop-down list successfully");
    }

    @Override
    public ResponseResult<ArrayList<Map<String, Object>>> getSexList() {
        ArrayList<Map<String, Object>> constantInformation=constantItemMapper.getSexList();
        return ResponseResult.getSuccessResult(constantInformation,"Get the sex information drop-down list successfully");
    }
}




