package com.SecondClass.server;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.json.JSONUtil;
import com.SecondClass.entity.ActicityApplication;
import com.SecondClass.entity.Activity;
import com.SecondClass.entity.R_entity.R_ActicityApplication;
import com.SecondClass.entity.Response;
import com.SecondClass.entity.ResponseStatus;
import com.SecondClass.mapper.ActicityApplicationMapper;
import com.SecondClass.mapper.ActivityMapper;
import com.alibaba.druid.support.json.JSONUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

@Slf4j
@Service
public class ActivityServerImpl implements IActivityServer{
    @Resource
    ActivityMapper activityMapper;
    @Resource
    ActicityApplicationMapper acticityApplicationMapper;
    @Resource
    StringRedisTemplate stringRedisTemplate;

    @Transactional
    public Response applyActivity(R_ActicityApplication request){
        try{
            //1.添加活动信息到数据库
            //  设置活动状态为1（表示审核中），添加活动信息到活动表中
            Activity activity = BeanUtil.copyProperties(request, Activity.class);
            activity.setAstatus(1);
            System.out.println(activity);
            //  插入失败抛出异常
            if (activityMapper.insert(activity) != 1) throw new IllegalArgumentException();

            //2.添加活动申请表到数据库
            //  将活动信息转换为json字符串存入申请表的活动描述
            String jsonStr_activity = JSONUtil.toJsonStr(activity);
            ActicityApplication acticityApplication = ActicityApplication.builder().aid(activity.getAid())
                    .uid(activity.getAUid())
                    .aAppDescription(jsonStr_activity)
                    .aAppAttachment(request.getAAppAttachment())
                    .aAppStatus(1).build();
            System.out.println(acticityApplication);
            //  插入失败抛出异常
            if (acticityApplicationMapper.insert(acticityApplication) != 1) throw new IllegalArgumentException();

            //将活动申请活动信息写入redis，以活动id作为hashKey
            String jsonStr_acticityApplication = JSONUtil.toJsonStr(acticityApplication);
            stringRedisTemplate.opsForHash().put("secondclass:activity:applyActivity",acticityApplication.getAid().toString(),jsonStr_acticityApplication);
            return Response.success(ResponseStatus.ACTIVITY_APPLY_SUCCESS);
        }catch (IllegalArgumentException i){
            //redis插入失败
            i.printStackTrace();
            return Response.error(ResponseStatus.ACTIVITY_APPLY_FAIL);
        }catch (DataIntegrityViolationException d){
            //数据库插入失败
            d.printStackTrace();
            return Response.error(ResponseStatus.ACTIVITY_APPLY_FAIL);
        }
        catch (Exception e){
            //其他错误
            e.printStackTrace();
            return Response.success(ResponseStatus.ERROR);
        }
    }
}
