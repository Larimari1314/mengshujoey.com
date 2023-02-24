package com.mengshujoey.mengshuadminsystem.backend.api;

import com.mengshujoey.mengshuadminsystem.backend.service.TimedTasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

/**
 * application name:mengshujoeyPatient - TimedTasks
 * application describing: 定时任务
 * copyright:
 * company:
 * time:2023-01-14 17:10:44
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
public class TimedTasks {
    @Autowired
    private TimedTasksService timedTasksService;

    /**
     * 每月十五号凌晨3点十分刷新数据（避免用户出现数据显示错误的消息）刷新公钥私钥数据
     */
    @Scheduled(cron = "0 15 3 10 * ?")
    public void refreshEncryptedData() {
        timedTasksService.refreshEncryptedData();
    }

}
