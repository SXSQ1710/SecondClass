package com.SecondClass.test.testApplicationEventPublisher;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.ArrayList;

/**
 * @title: SendEvents
 * @Author SXSQ
 * @Description //TODO
 * @Date 2023/11/5 21:42
 **/

@Component
public class SendEventsToApp {
    @EventListener
    public void sendMessage(SendMessageEvent event) {
        System.out.println("调用消息发送到APP" + event.getId());
    }
}
