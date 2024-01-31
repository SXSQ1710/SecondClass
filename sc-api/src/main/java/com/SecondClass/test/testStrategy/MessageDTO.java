package com.SecondClass.test.testStrategy;

import lombok.Data;

/**
 * @title: MessageDTO
 * @Author SXSQ
 * @Description //TODO
 * @Date 2023/11/4 20:38
 **/
@Data
public class MessageDTO {
    /**
     * 项目id
     */
    private String projectId;

    /**
     * 项目名称
     */
    private String projectName;

    /**
     * 标题
     */
    private String title;

    /**
     * 内容
     */
    private String content;

    /**
     * 消息类型 1消息 2待办 3报警
     */
    private Integer type;

    @Override
    public String toString() {
        return "MessageDTO{" +
                "projectId='" + projectId + '\'' +
                ", projectName='" + projectName + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", type=" + type +
                '}';
    }
}
