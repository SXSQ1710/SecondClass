-- 1.参数列表
-- 1.1活动id
local activityId = ARGV[1]
-- 1.2用户id
local userId = ARGV[2]
-- 1.3报名id
local participationId = ARGV[3]
-- 1.4活动参与状态码
local participationStatus = ARGV[4]

-- 2.数据key
-- 2.1剩余名额key
local stockKey = 'seckill:stock:' .. activityId
-- 2.2报名表key
local participationKey = 'seckill:participation:' .. activityId

-- 3.业务脚本
-- 3.1判断剩余名额是否大于0
if (tonumber(redis.call('get',stockKey)) <= 0) then
    -- 3.2.名额不足，返回1
    return 1
end
-- 3.2.判断用户是否报名过 zscore myzset three
if(tonumber(redis.call('zscore', participationKey,userId .. '-' .. participationStatus)) ~= nil)then
    --3.3.存在，说明重复报名，返回2
    return 2
end
--if(tonumber(redis.call('sismember', participationKey, userId .. '-' .. participationStatus)) == 1)then
--    --3.3.存在，说明重复报名，返回2
--    return 2
--end
--3.4.扣剩余名额
redis.call('incrby', stockKey, -1)
--3.5.保存报名信息 zadd myzset 1 one  zcard myzset
local score = redis.call('zcard',participationKey)
redis.call('zadd', participationKey, score, userId .. '-' .. participationStatus)
--redis.call('sadd', participationKey, userId .. '-' .. participationStatus)
--3.6.发生消息到队列中
redis.call('xadd','stream.participation','*','uid',userId,'aid',activityId,'pid',participationId)
return 0