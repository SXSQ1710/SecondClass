package com.SecondClass.test.testApplicationEventPublisher;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

/**
 * @title: SendEventsToH5
 * @Author SXSQ
 * @Description //TODO
 * @Date 2023/11/5 22:33
 **/

@Component
public class SendEventsToH5 {
    @EventListener
    public void sendMessage(SendMessageEvent event) {
        System.out.println("调用消息发送到H5" + event.getId());
    }
}
