package com.SecondClass.test.testStrategy.strategy;

import com.SecondClass.test.testStrategy.DailyCheck;
import com.SecondClass.test.testStrategy.MessageDTO;
import com.SecondClass.test.testStrategy.RemindConfig;

/**
 * @title: NotificationStrategy
 * @Author SXSQ
 * @Description //TODO
 * @Date 2023/11/4 19:29
 **/

public interface NotificationStrategy {
    String getType();
    MessageDTO createMessage(DailyCheck dailyCheck, RemindConfig remindConfig);
}
