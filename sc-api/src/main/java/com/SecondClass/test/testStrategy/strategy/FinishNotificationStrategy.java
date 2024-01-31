package com.SecondClass.test.testStrategy.strategy;

import com.SecondClass.test.testStrategy.DailyCheck;
import com.SecondClass.test.testStrategy.MessageDTO;
import com.SecondClass.test.testStrategy.RemindConfig;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

/**
 * @title: FinishNotificationStrategy
 * @Author SXSQ
 * @Description //TODO
 * @Date 2023/11/4 20:53
 **/

@Component
@Order(3)
public class FinishNotificationStrategy implements NotificationStrategy{
    @Override
    public String getType() {
        return "finish";
    }

    @Override
    public MessageDTO createMessage(DailyCheck dailyCheck, RemindConfig remindConfig) {
        System.out.println("生成审核完毕消息");
        MessageDTO messageDTO = new MessageDTO();
        messageDTO.setTitle("审核完毕");
        messageDTO.setContent("生成审核完毕消息");
        return messageDTO;
    }
}
