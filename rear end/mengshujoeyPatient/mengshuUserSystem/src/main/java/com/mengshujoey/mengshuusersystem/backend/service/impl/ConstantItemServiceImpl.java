package com.mengshujoey.mengshuusersystem.backend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.mengCommon.backend.dao.ConstantItemMapper;
import com.mengCommon.backend.form.RabbitMessage;
import com.mengCommon.backend.pojo.ConstantItem;
import com.mengCommon.common.common.http.RedisCache;
import com.mengshujoey.mengshuusersystem.backend.service.ConstantItemService;
import com.mengshujoey.mengshuusersystem.common.config.MessageException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.BoundHashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import java.util.concurrent.locks.ReentrantLock;

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
    private final static ReentrantLock CONSTANTITEMS = new ReentrantLock();
    private static final Logger log = LoggerFactory.getLogger(ConstantItemServiceImpl.class);
    @Autowired
    private RedisTemplate redisTemplate;

    @Override
    public void mainDataFlush(RabbitMessage message) {
        if (ObjectUtils.isEmpty(message.getId())) {
            //id为空啥都删除
            for (String redisName : message.getRedisCache()) {
                flushCode(redisName);
                //删除数据啦

                log.info("删除命令执行了{}",message);

                redisTemplate.delete(redisName);
            }
        } else {
            //id不得为空则对id进行操作
            log.info("删除命令执行了{}",message);
            for (String redisName : message.getRedisCache()) {
                flushCode(redisName);
                //删除key
                //判断id类型是否是数据或者集合
                if (message.getId().getClass().isArray()) {
                    //数组异常
                    log.error("数组类型不支持转化====>{}", message);
                    throw new MessageException("消息类型不支持转化!");
                } else {
                    for (String id : message.getId()) {
                        CONSTANTITEMS.lock();
                        try {
                            BoundHashOperations boundHashOperations = redisTemplate.boundHashOps(redisName);
                            if (boundHashOperations != null) {
                                if (boundHashOperations.hasKey(id)) {
                                    //存在
                                    boundHashOperations.delete(id);
                                }
                            }
                        } finally {
                            CONSTANTITEMS.unlock();
                        }
                    }
                }
            }
        }
    }

    private void flushCode(String redisName) {
        if (redisName.equals(RedisCache.MainEvaluationDetail.name())) {
            if (redisTemplate.boundValueOps(RedisCache.mainMickTeaCode.name()) != null) {
                redisTemplate.delete(RedisCache.mainMickTeaCode.name());
            }
        }
        if (redisName.equals(RedisCache.evaluationBasic.name())) {
            if (redisTemplate.boundValueOps(RedisCache.evaluationVideoCode.name()) != null) {
                redisTemplate.delete(RedisCache.evaluationVideoCode.name());
            }
        }
    }
}




