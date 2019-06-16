/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: RedisKeyUtil
 * Author:   LSN
 * Date:     2019/6/14 14:17
 * Description: 用于生成Redis的key，保证安全
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.utils;

/**
 * 〈一句话功能简述〉<br>
 * 〈用于生成Redis的key，保证安全〉
 *
 * @author LSN
 * @create 2019/6/14
 * @since 1.0.0
 */
public class RedisKeyUtil
{
    private static String SPLIT = ":";
    private static String BIZ_LIKE = "LIKE";
    private static String BIZ_DISLIKE = "DISLIKE";
    private static String BIZ_EVENTQUEUE = "EVENTQUEUE";
    //粉丝
    private static String BIZ_FOLLOWER = "FOLLOWER";
    //关注对象
    private static String BIZ_FOLLOWEE = "FOLLOWEE";
    private static String BIZ_TIMELINE = "TIMELINE";

    public static String getLikeKey(int entityType, int entityId)
    {
        return BIZ_LIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getDisLikeKey(int entityType, int entityId)
    {
        return BIZ_DISLIKE + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getFollowerKey(int entityType, int entityId)
    {
        return BIZ_FOLLOWER + SPLIT + String.valueOf(entityType) + SPLIT + String.valueOf(entityId);
    }

    public static String getFolloweeKey(int userId, int entityType)
    {
        return BIZ_FOLLOWEE + SPLIT + String.valueOf(userId) + SPLIT + String.valueOf(entityType);
    }

    public static String getEventQueueKey()
    {
        return BIZ_EVENTQUEUE;
    }

    public static String getTimelineKey(int userId)
    {
        return BIZ_TIMELINE + SPLIT + String.valueOf(userId);
    }
}
