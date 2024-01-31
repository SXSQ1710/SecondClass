package com.SecondClass.test.testStrategy.strategy;

import com.SecondClass.test.testStrategy.DailyCheck;
import com.SecondClass.test.testStrategy.MessageDTO;
import com.SecondClass.test.testStrategy.RemindConfig;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @title: OverTimeNotificationStrategy
 * @Author SXSQ
 * @Description //TODO
 * @Date 2023/11/4 20:39
 **/

@Component
@Order(1)
public class OverTimeNotificationStrategy implements NotificationStrategy {
    @Override
    public String getType() {
        return "overTime";
    }

    @Override
    public MessageDTO createMessage(DailyCheck dailyCheck, RemindConfig remindConfig) {
        System.out.println("生成超时整改通知消息");
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setTitle("超时整改");
        messageDTO.setContent("生成超时整改通知消息");
        return messageDTO;
    }
}
