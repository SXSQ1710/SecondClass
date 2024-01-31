package com.SecondClass.test.testStrategy.strategy;

import com.SecondClass.test.testStrategy.DailyCheck;
import com.SecondClass.test.testStrategy.MessageDTO;
import com.SecondClass.test.testStrategy.RemindConfig;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @title: ImproveNotificationStrategy
 * @Author SXSQ
 * @Description //TODO
 * @Date 2023/11/4 20:37
 **/

@Component
@Order(2)
public class ImproveNotificationStrategy implements NotificationStrategy {
    @Override
    public String getType() {
        return "improve";
    }

    @Override
    public MessageDTO createMessage(DailyCheck dailyCheck, RemindConfig remindConfig) {
        // 生成整改待办消息
        System.out.println("生成整改待办消息");
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setTitle("待办消息");
        messageDTO.setContent("生成整改待办消息");
        return messageDTO;
    }
}
