package com.SecondClass.test.testStrategy;

import com.SecondClass.entity.Response;
import com.SecondClass.entity.ResponseStatus;
import com.SecondClass.test.testStrategy.strategy.NotificationStrategy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @title: MessageController
 * @Author SXSQ
 * @Description //TODO
 * @Date 2023/11/4 20:43
 **/

@RestController
@RequestMapping(value = "/api/message")
public class MessageController {
    @Resource
    NotificationStrategyFactory notificationStrategyFactory;


    @GetMapping("/{type}")
    public Response dataBaseClear(@PathVariable("type") String type) {
        NotificationStrategy strategy = notificationStrategyFactory.getStrategy(type);
        MessageDTO message = strategy.createMessage(new DailyCheck(), new RemindConfig());
        MessageServer.sendMessage(message);
        return Response.success(ResponseStatus.SUCCESS, "操作成功！");
    }
}
