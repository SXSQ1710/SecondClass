package com.SecondClass.server;

import com.SecondClass.entity.Response;
import com.SecondClass.entity.SelfApplication;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
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

    /**
     * 审核申请表
     * @param selfAppId 自主申报表Id
     * @param statue 审核状态 2：审核中 1：通过 0：拒绝
     * @return Response
     */
    Response auditSelfApplication(Integer selfAppId,Integer statue);

    /**
     * 查看申报结果（即浏览申报记录）
     * @param uid 用户id
     * @param page 分页
     * @return Response
     */
    Response getMySelfApplication(Integer uid, Page<SelfApplication> page);

    /**
     * 修改表格
     * @param selfApplication
     * @return
     */
    Response updateSelfApplication(SelfApplication selfApplication);
}
