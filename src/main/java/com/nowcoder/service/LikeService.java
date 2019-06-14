/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: LikeService
 * Author:   LSN
 * Date:     2019/6/14 14:14
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.service;

import com.nowcoder.utils.JedisAdapter;
import com.nowcoder.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 〈一句话功能简述〉<br>
 * 〈使用的redis的集合，其中前面的key为集合名称，后面的value为加入集合的元素，
 * 从而每一个用户只能喜欢/不喜欢一次，针对每一个评论号建立专有的集合〉
 *
 * @author LSN
 * @create 2019/6/14
 * @since 1.0.0
 */
@Service
public class LikeService
{
    @Autowired
    JedisAdapter jedisAdapter;

    public long getLikeCount(int entityType, int entityId)
    {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        return jedisAdapter.scard(likeKey);
    }

    public int getLikeStatus(int userId, int entityType, int entityId)
    {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        if (jedisAdapter.sismember(likeKey, String.valueOf(userId)))
        {
            return 1;
        }
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        if (jedisAdapter.sismember(disLikeKey, String.valueOf(userId)))
            return -1;
        return 0;
    }

    public long like(int userId, int entityType, int entityId)
    {
        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.sadd(likeKey, String.valueOf(userId));

        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.srem(disLikeKey, String.valueOf(userId));
        return jedisAdapter.scard(likeKey);
    }

    public long disLike(int userId, int entityType, int entityId)
    {
        String disLikeKey = RedisKeyUtil.getDisLikeKey(entityType, entityId);
        jedisAdapter.sadd(disLikeKey, String.valueOf(userId));

        String likeKey = RedisKeyUtil.getLikeKey(entityType, entityId);
        jedisAdapter.srem(likeKey, String.valueOf(userId));

        return jedisAdapter.scard(likeKey);
    }
}
