package com.mengshujoey.mengshuusersystem.backend.service.impl;

import com.mengCommon.backend.dao.DailyActivityRecordsMapper;
import com.mengCommon.backend.dao.EvaluationBasicInformationMapper;
import com.mengCommon.backend.form.RabbitMessage;
import com.mengCommon.backend.pojo.DailyActivityRecords;
import com.mengCommon.common.common.http.RabbitStatus;
import com.mengCommon.common.common.http.RedisCache;
import com.mengCommon.common.common.http.ResponseResult;
import com.mengCommon.common.utils.IdWorker;
import com.mengshujoey.mengshuusersystem.backend.service.ConstantItemService;
import com.mengshujoey.mengshuusersystem.backend.service.TimedTasksService;
import com.mengshujoey.mengshuusersystem.common.config.Statistic;
import org.apache.commons.lang.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.HyperLogLogOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * application name:mengshujoeyPatient - TimedTasksServiceImpl
 * application describing:
 * copyright:
 * company:
 * time:2023-02-11 10:23:08
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@Service
public class TimedTasksServiceImpl implements TimedTasksService {
    public final static String REDIS_ENCRYPTED_DATA = "encryptedDataByUserPage";
    public final static String REDIS_KEY_STRING = "KeyString";
    private static final Logger log = LoggerFactory.getLogger(TimedTasksServiceImpl.class);
    @Autowired
    private RedisTemplate redisTemplate;
    @Autowired
    private DailyActivityRecordsMapper dailyActivityRecordsMapper;
    @Autowired
    private EvaluationBasicInformationMapper evaluationBasicInformationMapper;
    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Value("${redisCache.cacheEncryption}")
    private Boolean redisCache;
    @Autowired
    private ConstantItemService constantItemService;

    @Override
    public void refreshEncryptedData() {
        String privateKeyString = RandomStringUtils.randomAlphanumeric(16);
        BoundHashOperations encryptedData = redisTemplate.boundHashOps(TimedTasksServiceImpl.REDIS_ENCRYPTED_DATA);
        //更新私钥缓存
        if (redisCache) {
            //采用方案一则删除全部缓存数据
            //rabbitMQ发送更新数据
            RabbitMessage rabbitMessage = new RabbitMessage().setId(null).setRedisCache(Arrays.asList(RedisCache.values()).stream().map(s -> {
                return s.name();
            }).collect(Collectors.toList())).setStatus(RabbitStatus.ADD.name());
            constantItemService.mainDataFlush(rabbitMessage);
        }
        encryptedData.put(TimedTasksServiceImpl.REDIS_KEY_STRING, privateKeyString);
    }

    @Override
    public ResponseResult<String> obtainPrivateKey() {
        Object publicKeyObject = redisTemplate.boundHashOps(TimedTasksServiceImpl.REDIS_ENCRYPTED_DATA).get(TimedTasksServiceImpl.REDIS_KEY_STRING);
        if (publicKeyObject == null) {
            synchronized (this) {
                Object publicKey = redisTemplate.boundHashOps(TimedTasksServiceImpl.REDIS_ENCRYPTED_DATA).get(TimedTasksServiceImpl.REDIS_KEY_STRING);
                if (publicKey == null) {
                    refreshEncryptedData();
                }
            }
            return obtainPrivateKey();
        }
        return ResponseResult.getSuccessResult(publicKeyObject.toString(), null);
    }

    @Override
    public void DailyActivityStatistics() {
        HyperLogLogOperations<String, String> hyperlog = redisTemplate.opsForHyperLogLog();
        int count = hyperlog.size(Statistic.user_total_size_today.name()).intValue();
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        hyperlog.union(Statistic.user_total_size.name(), Statistic.user_total_size_today.name());
        int totalCount = hyperlog.size(Statistic.user_total_size.name()).intValue();
        //存入数据库中
        DailyActivityRecords dailyActivityRecords = DailyActivityRecords.builder()
                .id(new IdWorker().nextId() + "")
                .count(count)
                .date(new Date())
                .totalCount(totalCount + "");
        //添加到数据库中
        dailyActivityRecordsMapper.insert(dailyActivityRecords);
        log.info("日活量信息入库，昨日数据:[{}]，总数据:[{}]", count, totalCount);
        hyperlog.delete(Statistic.user_total_size_today.name());
    }

    @Override
    public void countIndividualClicks() {
        BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(Statistic.project_click_number.name());
        Set<String> keys = boundHashOperations.keys();
        for (String key : keys) {
            log.debug("点击视频id：{},点击量：{}", key, boundHashOperations.get(key));
            //更新数据库
            evaluationBasicInformationMapper.countIndividualClicks(key, Integer.parseInt(boundHashOperations.get(key) + ""));
        }
        redisTemplate.delete(Statistic.project_click_number.name());
    }
}
