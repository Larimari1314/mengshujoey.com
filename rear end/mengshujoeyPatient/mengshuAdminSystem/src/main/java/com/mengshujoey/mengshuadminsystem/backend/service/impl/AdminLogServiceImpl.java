package com.mengshujoey.mengshuadminsystem.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mengCommon.backend.dao.AdminLogMapper;
import com.mengCommon.backend.form.QueryEvaluationDetailByUser;
import com.mengCommon.backend.pojo.AdminLog;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengCommon.common.utils.IdWorker;
import com.mengshujoey.mengshuadminsystem.backend.service.AdminLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.locks.StampedLock;

/**
 * application name:mengshujoeyPatient - AdminLogServiceImpl
 * application describing:
 * copyright:
 * company:
 * time:2023-01-14
 *
 * @author liujingwen
 * @version ver 1.0
 * @description 针对表【admin_log】的数据库操作Service实现
 * @createDate 2023-01-14 15:07:49
 * @since 1.8
 */
@Service
public class AdminLogServiceImpl extends ServiceImpl<AdminLogMapper, AdminLog>
        implements AdminLogService {
    @Autowired
    private AdminLogMapper adminLogMapper;

    private final StampedLock stampedLock=new StampedLock();
    @Override
    public void recordOperationLog(String adminId, Date operationTime, String level, String content) {
        //记录操作日志，考虑存在多个线程所以加锁处理
        //加入写锁
        long lockWriter = stampedLock.writeLock();
        try{
            //写入数据
            long id = new IdWorker().nextId();
            //添加数据
            adminLogMapper.recordOperationLog(id,adminId,operationTime,level,content);
        }finally {
            stampedLock.unlockWrite(lockWriter);
        }

    }

    @Override
    public ResponseResult<PageInfo<Map<String, Object>>> findAdminLog(QueryEvaluationDetailByUser queryEvaluationDetail) {
        PageHelper.startPage(queryEvaluationDetail.getPage(), 10);
        ArrayList<Map<String, Object>> arrayList = adminLogMapper.queryLog();
        PageInfo<Map<String,Object>> pageInfo=new PageInfo<>(arrayList);
        return ResponseResult.getSuccessResult(pageInfo, "The query succeeded");
    }
}




