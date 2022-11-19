package com.SecondClass.server;

import cn.hutool.core.bean.BeanUtil;
import com.SecondClass.entity.Participation;
import com.SecondClass.mapper.ParticipationMapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.connection.stream.*;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @title: MessagesQueueHandler
 * @Author SXSQ
 * @Description //TODO
 * @Date 2022/11/15 16:14
 **/
@Slf4j
@Component
public class MessagesQueueHandler {
    @Resource
    StringRedisTemplate stringRedisTemplate;
    @Resource
    ParticipationMapper participationMapper;

    private static final ExecutorService SECKILL_EXECUTOR = Executors.newSingleThreadExecutor();
    private static final ExecutorService SIGN_IN_OFF = Executors.newSingleThreadExecutor();

    @PostConstruct
    private void init(){
        SECKILL_EXECUTOR.submit(new VoucherParticipationHandler());
        SIGN_IN_OFF.submit(new SignHandler());
        if (!stringRedisTemplate.hasKey("stream.participation")){
            stringRedisTemplate.opsForStream().createGroup("stream.participation", "g1");
        }
        if (!stringRedisTemplate.hasKey("stream.sign")){
            stringRedisTemplate.opsForStream().createGroup("stream.sign", "g1");
        }
    }

//    @Async("taskExecutor")
//    @Scheduled(fixedDelay = 300)
//    public void test(){
//        System.out.println("---------------------");
//    }

    private class SignHandler implements Runnable{
        String queueName = "stream.sign" ;
        @Override
        public void run() {
            while (true){
                try{
                    //1.获取消息队列中的报名信息
                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
                            Consumer.from("g1", "c1"),
                            StreamReadOptions.empty().count(1).block(Duration.ofSeconds(1)),
                            StreamOffset.create(queueName, ReadOffset.lastConsumed())
                    );
                    //2.判断消息获取是否成功
                    if (list == null || list.isEmpty()){
                        //2.1如果为空表示消息队列中没有消息，继续下一个循环
                        continue;
                    }

                    //3.解析数据
                    MapRecord<String, Object, Object> record = list.get(0);
                    Map<Object, Object> value = record.getValue();
                    if (value.containsKey("i")){
                        String[] info = value.get("i").toString().split(",");
                        //userId .. ',' .. activityId .. ',' .. participationStatus
                        Participation participation = Participation.builder()
                                .uid(Long.valueOf(info[0]))
                                .aid(Long.valueOf(info[1]))
                                .participateStatus(Integer.valueOf(info[2])).build();
                        //4.写入数据库
                        updateStatus(participation);
                    } else if (value.containsKey("o")){
                        String[] info = value.get("o").toString().split(",");
                        //userId .. ',' .. activityId .. ',' .. participationStatus
                        Participation participation = Participation.builder()
                                .uid(Long.valueOf(info[0]))
                                .aid(Long.valueOf(info[1]))
                                .participateStatus(Integer.valueOf(info[2])).build();
                        //4.写入数据库
                        updateStatus(participation);
                    }
                    //5.ack确认
                    stringRedisTemplate.opsForStream().acknowledge(queueName,"g1",record.getId());
                }catch (Exception e){
                    log.error("处理PendingList报名异常", e);
                }
            }
        }

        private void updateStatus(Participation participation){
            UpdateWrapper<Participation> updateWrapper = new UpdateWrapper<>();
            updateWrapper.eq("aid",participation.getAid()).eq("uid",participation.getUid());
            updateWrapper.set("participate_status",participation.getParticipateStatus());
            if (participationMapper.update(null,updateWrapper) != 1) throw new IllegalArgumentException();
        }
    }

    private class VoucherParticipationHandler implements Runnable{
        String queueName = "stream.participation" ;

        @Override
        public void run() {
            while (true){
                try{
                    //1.获取消息队列中的报名信息
                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
                            Consumer.from("g1", "c1"),
                            StreamReadOptions.empty().count(1).block(Duration.ofSeconds(1)),
                            StreamOffset.create(queueName, ReadOffset.lastConsumed())
                    );
                    //2.判断消息获取是否成功
                    if (list == null || list.isEmpty()){
                        //2.1如果为空表示消息队列中没有消息，继续下一个循环
                        continue;
                    }

                    //3.解析数据
                    MapRecord<String, Object, Object> record = list.get(0);
                    Map<Object, Object> value = record.getValue();
                    Participation participation = BeanUtil.fillBeanWithMap(value, new Participation(), true);
                    //4.写入数据库
                    saveParticipation(participation);
                    //5.ack确认
                    stringRedisTemplate.opsForStream().acknowledge(queueName,"g1",record.getId());
                }catch (Exception e){
                    log.error("处理PendingList报名异常", e);
                    handlePendingList();
                }
            }
        }

        private void handlePendingList(){
            while (true){
                try{
                    //1.获取消息队列中的报名信息
                    List<MapRecord<String, Object, Object>> list = stringRedisTemplate.opsForStream().read(
                            Consumer.from("g1", "c1"),
                            StreamReadOptions.empty().count(1),
                            StreamOffset.create(queueName, ReadOffset.from("0"))
                    );
                    //2.判断消息获取是否成功
                    if (list == null || list.isEmpty()){
                        //2.1如果为空表示PendingList中没有异常消息了，继续下一个循环
                        break;
                    }
                    //3.解析数据
                    MapRecord<String, Object, Object> record = list.get(0);
                    Map<Object, Object> value = record.getValue();
                    Participation participation = BeanUtil.fillBeanWithMap(value, new Participation(), true);
                    //4.写入数据库
                    saveParticipation(participation);
                    //5.ack确认
                    stringRedisTemplate.opsForStream().acknowledge(queueName,"g1",record.getId());
                }catch (Exception e){
                    log.error("处理PendingList报名异常", e);
                    try {
                        Thread.sleep(20);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }
                }
            }
        }

        private void saveParticipation(Participation participation){
            participation.setParticipateStatus(0);
            if(participationMapper.insert(participation) != 1) throw new IllegalArgumentException();
        }
    }
}
