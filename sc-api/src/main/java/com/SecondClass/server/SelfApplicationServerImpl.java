package com.SecondClass.server;

import cn.hutool.core.util.IdUtil;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.ResponseStatus;
import com.SecondClass.entity.SelfApplication;
import com.SecondClass.mapper.SelfApplicationMapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;

@Service
public class SelfApplicationServerImpl implements ISelfApplicationServer {

    @Resource
    private SelfApplicationMapper selfApplicationMapper;

    @Value("${self-attachment}")
    private String basePath;

    @Override
    public Response addSelfApplication(SelfApplication selfApplication) {
        //直接插入申报表信息
        int insert = selfApplicationMapper.insert(selfApplication);
        return insert>0 ? Response.success(ResponseStatus.SUCCESS) : Response.error(ResponseStatus.ERROR);
    }

    @Override
    public Response addSelfApplicationAttachment(MultipartFile attachment) {
        //attachment是一个临时文件，需要转存到指定的位置，否则本次请求完成后临时文件会被删除
        //原始附件名
        String originalFilename = attachment.getOriginalFilename();
        //后缀
        String suffix = originalFilename.substring(originalFilename.lastIndexOf("."));
        //使用UUID生成附件名
        String fileName = IdUtil.simpleUUID() + suffix;
        String path = basePath+fileName;
        try {
            //将临时文件转存到指定位置
            attachment.transferTo(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
            return Response.error(ResponseStatus.ERROR);
        }
        return Response.success(ResponseStatus.SUCCESS,path);
    }

    @Override
    public Response auditSelfApplication(Integer selfAppId, Integer statue) {
        UpdateWrapper<SelfApplication> updateWrapper = new UpdateWrapper<>();
        updateWrapper.eq("self_app_id",selfAppId)
                     .set("self_app_statu",statue);
        int update = selfApplicationMapper.update(null, updateWrapper);
        return update>0 ? Response.success(ResponseStatus.SUCCESS) : Response.error(ResponseStatus.ERROR);
    }

    @Override
    public Response getMySelfApplication(Integer uid, Page<SelfApplication> page) {
        QueryWrapper<SelfApplication> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("uid",uid);
        Page<SelfApplication> ret = selfApplicationMapper.selectPage(page,queryWrapper);
        return Response.success(ResponseStatus.SUCCESS,ret);
    }
}
