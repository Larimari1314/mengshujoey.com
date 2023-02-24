package com.mengshujoey.mengshuadminsystem.backend.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.mengCommon.backend.pojo.DailyActivityRecords;
import com.mengCommon.common.common.http.ResponseResult;

import java.util.ArrayList;
import java.util.Map;

/**
* @author 86155
* @description 针对表【daily_activity_records】的数据库操作Service
* @createDate 2023-02-20 18:41:55
*/
public interface DailyActivityRecordsService extends IService<DailyActivityRecords> {

    ResponseResult<ArrayList<Map<String, Object>>> queryDailyActivity();

}
