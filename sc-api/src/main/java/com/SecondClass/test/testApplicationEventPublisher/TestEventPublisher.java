package com.SecondClass.test.testApplicationEventPublisher;

import com.SecondClass.entity.Response;
import com.SecondClass.entity.ResponseStatus;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @title: TestEventPublisher
 * @Author SXSQ
 * @Description //TODO
 * @Date 2023/11/5 21:45
 **/

@RestController
@RequestMapping(value = "/api/messageEvent")
public class TestEventPublisher {
    @Resource
    private ApplicationEventPublisher publisher;

    @GetMapping(value = "/test")
    public Response test(){
        SendMessageEvent sendMessageEvent = new SendMessageEvent();
        sendMessageEvent.setId("123");
        publisher.publishEvent(sendMessageEvent);
        return Response.success(ResponseStatus.SUCCESS);
    }
}
