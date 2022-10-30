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
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
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
            //1.生成活动描述
            //  将活动信息转换为json字符串存入申请表的活动描述
            Activity activity = BeanUtil.copyProperties(request, Activity.class);
            System.out.println(activity);
            String jsonStr_activity = JSONUtil.toJsonStr(activity);

            //2.添加活动申请表到数据库
            //  设置申请状态为1
            ActicityApplication acticityApplication = ActicityApplication.builder().uid(activity.getAUid())
                    .aAppDescription(jsonStr_activity)
                    .aAppAttachment(request.getAAppAttachment())
                    .aAppStatus(1).build();
            System.out.println(acticityApplication);
            //  插入失败抛出异常
            if (acticityApplicationMapper.insert(acticityApplication) != 1) throw new IllegalArgumentException();

            //将活动申请活动信息写入redis，以活动id作为hashKey
            String jsonStr_acticityApplication = JSONUtil.toJsonStr(acticityApplication);
            stringRedisTemplate.opsForHash().put("secondclass:activity:applyActivity",acticityApplication.getAAppId().toString(),jsonStr_acticityApplication);
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

    @Transactional
    public Response auditActivity(Integer aAppId,Integer status,String explain) {
        try {
            //1.更新活动申请实体和活动实体的状态
            //1.1 获得活动实体
            QueryWrapper<ActicityApplication> queryWrapper = new QueryWrapper<>();
            queryWrapper.eq("a_app_id", aAppId);
            ActicityApplication acticityApplication = acticityApplicationMapper.selectOne(queryWrapper);

            //1.2修改活动申请状态   2表示通过 0：表示拒绝
            acticityApplication.setAAppStatus(status);
            if(explain != null){
                acticityApplication.setAAppExplain(explain);
            }else{
                acticityApplication.setAAppExplain("无");
            }

            //2.如果活动通过的话，将活动存入活动表中
            if(status == 2){
                //2.1添加活动到活动表 并将状态设置为1（活动未开始）
                String json = acticityApplication.getAAppDescription();
                Activity activity = JSONUtil.toBean(json, Activity.class);
                activity.setAstatus(status);
                if(activityMapper.insert(activity) !=1)throw new IllegalArgumentException();
                //2.2 缓存存入活动表中
                stringRedisTemplate.opsForHash().put("secondclass:activity:activity",activity.getAid().toString(),JSONUtil.toJsonStr(activity));

                //2.3新的活动状态(多了一个aid）更新到活动描述中
                String jsonStr_activity = JSONUtil.toJsonStr(activity);
                acticityApplication.setAAppDescription(jsonStr_activity);

            }
            //2.5 更新活动申请表
            if(acticityApplicationMapper.updateById(acticityApplication)!=1)throw new IllegalArgumentException();

            //3.修改redis里面的数据
            //3.1 活动申请表
            stringRedisTemplate.opsForHash().delete("secondclass:activity:applyActivity",acticityApplication.getAAppId().toString());
            stringRedisTemplate.opsForHash().put("secondclass:activity:applyActivity",acticityApplication.getAAppId().toString(),JSONUtil.toJsonStr(acticityApplication));

            return Response.success(ResponseStatus.ADUIT_SUCCESS);
        } catch (IllegalArgumentException i) {
            i.printStackTrace();
            return Response.error(ResponseStatus.ADUIT_FAIL);
        } catch (DataIntegrityViolationException d) {
            d.printStackTrace();
            return Response.error(ResponseStatus.ADUIT_FAIL);
        } catch (Exception e) {
            //其他错误
            e.printStackTrace();
            return Response.success(ResponseStatus.ERROR);
        }
    }
}
