package com.mengshujoey.mengshuusersystem.backend.api;

import com.mengCommon.backend.form.RabbitMessage;
import com.mengCommon.common.config.RabbitQueuesConfig;
import com.mengshujoey.mengshuusersystem.backend.service.ConstantItemService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * application name:mengshujoeyPatient - ConsumeMessages
 * application describing:
 * copyright:
 * company:
 * time:2023-02-09 09:42:07
 *
 * @author liujingwen
 * @version ver 1.0
 * @since 1.8
 */
@RestController
public class ConsumeMessages {
    @Autowired
    private ConstantItemService constantItemService;

    /**
     * 删除缓存信息
     * @param message
     */
    @RabbitListener(queues = RabbitQueuesConfig.USER_DATA_FLUSH)
    public void mainDataFlush(@RequestBody RabbitMessage message) {
        constantItemService.mainDataFlush(message);
    }
}
