/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: feedHandler
 * Author:   LSN
 * Date:     2019/6/17 16:33
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.async.handler;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.Feed;
import com.nowcoder.model.Question;
import com.nowcoder.model.User;
import com.nowcoder.service.FeedService;
import com.nowcoder.service.FollowService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import com.nowcoder.utils.JedisAdapter;
import com.nowcoder.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/17
 * @since 1.0.0
 */
@Component
public class feedHandler implements EventHandler
{
    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    FeedService feedService;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    private String buildFeedData(EventModel model)
    {
        Map<String, String> map = new HashMap<>();
        User actor = userService.getUser(model.getActorId());
        if (actor == null)
            return null;
        map.put("userId", String.valueOf(actor.getId()));
        map.put("userHead", actor.getHeadUrl());
        map.put("userName", actor.getName());
        //要关注问题才行，关注人就不行了
        if (model.getType() == EventType.COMMENT ||
                (model.getType() == EventType.FOLLOW && model.getEntityType() == EntityType.ENTITY_QUESTION))
        {
            Question question = questionService.getQuestionById(model.getEntityId());
            if (question == null)
                return null;
            map.put("questionId", String.valueOf(question.getId()));
            map.put("questionTitle", question.getTitle());
            return JSONObject.toJSONString(map);
        }
        return null;
    }

    @Override
    public void doHandle(EventModel model)
    {
//        Random r = new Random();
//        model.setActorId(1 + r.nextInt(10));
        //生成事件feed流，对应着拉模式
        Feed feed = new Feed();
        feed.setCreatedDate(new Date());
        feed.setType(model.getType().getValue());
        feed.setData(buildFeedData(model));
        feed.setUserId(model.getActorId());
        if (feed.getData() == null)
            return;
        feedService.addFeed(feed);

        //给事件的粉丝推
        List<Integer> followers = followService.getFollowers(EntityType.ENTITY_USER, model.getActorId(),
                                                             Integer.MAX_VALUE);
        //表示系统
        followers.add(0);
        for (int follower : followers)
        {
            String timelineKey = RedisKeyUtil.getTimelineKey(follower);
            jedisAdapter.lpush(timelineKey, String.valueOf(feed.getId()));
            System.out.println("follower="+follower);
        }
    }

    @Override
    public List<EventType> getSupportEventTypes()
    {
        return Arrays.asList(new EventType[]{EventType.COMMENT, EventType.FOLLOW});
    }
}
