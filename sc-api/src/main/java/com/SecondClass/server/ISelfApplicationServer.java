package com.SecondClass.server;

import com.SecondClass.entity.Response;
import com.SecondClass.entity.SelfApplication;
import org.springframework.web.multipart.MultipartFile;

public interface ISelfApplicationServer{

    /**
     * 提交自主申报表
     * @param selfApplication 自主申报表对象
     * @return Response
     */
    Response addSelfApplication(SelfApplication selfApplication);

    /**
     * 提交附件
     * @param attachment 附件对象
     * @return Response
     */
    Response addSelfApplicationAttachment(MultipartFile attachment);
}
