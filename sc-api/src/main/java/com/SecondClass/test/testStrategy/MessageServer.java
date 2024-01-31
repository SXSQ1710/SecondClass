package com.SecondClass.test.testStrategy;

/**
 * @title: MessageServer
 * @Author SXSQ
 * @Description //TODO
 * @Date 2023/11/4 19:05
 **/

public class MessageServer {
    public static void sendMessage(MessageDTO todoDTO){
        String str = "消息接受：" + todoDTO.toString();
        System.out.println(str);
    }
}
