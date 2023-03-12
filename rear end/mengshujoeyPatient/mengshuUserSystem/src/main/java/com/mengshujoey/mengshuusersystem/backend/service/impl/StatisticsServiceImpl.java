package com.mengshujoey.mengshuusersystem.backend.service.impl;

import com.mengCommon.common.common.http.ResponseResult;
import com.mengshujoey.mengshuusersystem.backend.service.StatisticsService;
import com.mengshujoey.mengshuusersystem.common.config.Statistic;
import com.mengshujoey.mengshuusersystem.common.sercurity.utils.HttpUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.concurrent.locks.ReentrantLock;

/**
 * application name:mengshujoeyPatient - StatisticsServiceImpl
 * application describing:
 * copyright:
 * company:
 * time:2023-02-20 18:17:43
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@Service
public class StatisticsServiceImpl implements StatisticsService {
    private static final Logger log = LoggerFactory.getLogger(StatisticsServiceImpl.class);
    private static final ReentrantLock reentrantLock = new ReentrantLock();
    @Autowired
    private RedisTemplate redisTemplate;
    @Override
    public ResponseResult<String> countDailyActivity(HttpServletRequest request) {
        String ipAdress = HttpUtils.getIpAddress(request);
        log.debug("访问的ip地址为:[{}]", ipAdress);
        //将ip存入redis
        HyperLogLogOperations<String, String> hyperlog = redisTemplate.opsForHyperLogLog();
        hyperlog.add(Statistic.user_total_size_today.name(), ipAdress);
        return ResponseResult.getSuccessResult(null, "Record success");
    }

    @Override
    public ResponseResult<String> individualClicks(String projectId) {
        BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(Statistic.project_click_number.name());
        //判断key是否存在
        reentrantLock.lock();
        try {
            if (boundHashOperations.hasKey(projectId)) {
                int number = Integer.parseInt((String) boundHashOperations.get(projectId)) + 1;
                boundHashOperations.put(projectId, number);
            } else {
                boundHashOperations.put(projectId, "1");
            }
        } finally {
            reentrantLock.unlock();
        }
        return ResponseResult.getSuccessResult(null, "Record success");
    }
}
