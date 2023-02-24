package com.mengshujoey.mengshuadminsystem.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengCommon.backend.dao.DailyActivityRecordsMapper;
import com.mengCommon.backend.pojo.DailyActivityRecords;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengshujoey.mengshuadminsystem.backend.service.DailyActivityRecordsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

/**
* @author 86155
* @description 针对表【daily_activity_records】的数据库操作Service实现
* @createDate 2023-02-20 18:41:55
*/
@Service
public class DailyActivityRecordsServiceImpl extends ServiceImpl<DailyActivityRecordsMapper, DailyActivityRecords>
    implements DailyActivityRecordsService {
    @Autowired
    private DailyActivityRecordsMapper dailyActivityRecordsMapper;
    @Override
    public ResponseResult<ArrayList<Map<String, Object>>> queryDailyActivity() {
        ArrayList<Map<String, Object>> arrayList = dailyActivityRecordsMapper.queryDailyActivity();
        return ResponseResult.getSuccessResult(arrayList, "query success");
    }
}




