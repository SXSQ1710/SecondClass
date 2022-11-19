---
--- Generated by Luanalysis
--- Created by 胡毅.
--- DateTime: 2022/11/19 15:03
---
-- 1.参数列表
-- 1.1活动id
local activityId = ARGV[1]
-- 1.2用户id
local userId = ARGV[2]
-- 1.3活动参与状态码
local participationStatus = ARGV[3]

-- 2.数据key
-- 2.1报名表key
local participationKey = 'seckill:participation:' .. activityId

-- 3.业务脚本
-- 3.1.判断用户是否报名过 zscore myzset three
if(tonumber(redis.call('zscore', participationKey,userId .. '-' .. '0')) == nil)then
    --3.2.不存在，说明没有报名，返回1
    return 1
end
--获取对应报名的序号 zscore myzset two
local score = redis.call('zscore', participationKey, userId .. '-' .. '0')
--删除旧数据 zrem myzset two
redis.call('zrem', participationKey, userId .. '-' .. '0')
--添加新数据 zadd myzset 1 one
redis.call('zadd', participationKey, score,  userId .. '-' .. participationStatus)
--发送消息到队列中
--redis.call('xadd','stream.sign', '*', 'i', userId .. ',' .. activityId .. ',' .. participationStatus)
return 0