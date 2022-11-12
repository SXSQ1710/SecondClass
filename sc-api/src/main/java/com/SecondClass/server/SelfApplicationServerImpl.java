package com.SecondClass.server;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.ResponseStatus;
import com.SecondClass.entity.SelfApplication;
import com.SecondClass.entity.Shichang;
import com.SecondClass.mapper.SelfApplicationMapper;
import com.SecondClass.mapper.ShiChangMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.toolkit.Wrappers;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import java.io.File;
import java.io.IOException;
import java.util.Date;

@Service
public class SelfApplicationServerImpl implements ISelfApplicationServer {

    @Resource
    private SelfApplicationMapper selfApplicationMapper;

    @Resource
    private ShiChangMapper shiChangMapper;

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
    @Transactional
    public Response auditSelfApplication(Integer selfAppId, Integer statue) {
        LambdaQueryWrapper<SelfApplication> queryWrapper = Wrappers.<SelfApplication>lambdaQuery()
                        .eq(SelfApplication::getSelfAppId,selfAppId);
        SelfApplication selfApplication = selfApplicationMapper.selectOne(queryWrapper);
        selfApplication.setSelfAppStatu(statue);
        //如果审核通过则发放时长
        if (statue==2) {
            Shichang shichang = new Shichang();
            shichang.setUid(selfApplication.getUid());
            shichang.setSid(selfApplication.getSelfAppType());
            shichang.setSnum(selfApplication.getSelfAppShiNum());
            shichang.setAcquireTime(new Date());
            shiChangMapper.insert(shichang);
        }
        int update = selfApplicationMapper.updateById(selfApplication);
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
