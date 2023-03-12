package com.mengshujoey.mengshuusersystem.backend.api;

import com.mengshujoey.mengshuusersystem.backend.service.TimedTasksService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

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
@Component
public class TimedTasks {
    @Autowired
    private TimedTasksService timedTasksService;

    /**
     * 每月十五号凌晨3点十分刷新数据（避免用户出现数据显示错误的消息）刷新公钥私钥数据
     */
//    @Scheduled(cron = "*/5 * * * * ?")
    @Scheduled(cron = "0 15 3 10 * ?")
    public void refreshEncryptedData() {
        timedTasksService.refreshEncryptedData();
    }

    /**
     * 凌晨一点刷新点击量
     */
    @Scheduled(cron = "0 0 0 * * ?")
//    @Scheduled(cron = "*/5 * * * * ?")
    public void DailyActivityStatistics() {
        timedTasksService.DailyActivityStatistics();
    }

    @Scheduled(cron = "0 */10 * * * ?")
//    @Scheduled(cron = "*/5 * * * * ?")
    public void countIndividualClicks() {
        timedTasksService.countIndividualClicks();
    }
}
