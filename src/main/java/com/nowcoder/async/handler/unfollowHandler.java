/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: followHandler
 * Author:   LSN
 * Date:     2019/6/16 14:02
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.service.FeedService;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import com.nowcoder.utils.JedisAdapter;
import com.nowcoder.utils.RedisKeyUtil;
import com.nowcoder.utils.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/16
 * @since 1.0.0
 */
@Component
public class unfollowHandler implements EventHandler
{
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Autowired
    JedisAdapter jedisAdapter;

    @Autowired
    FeedService feedService;

    @Override
    public void doHandle(EventModel model)
    {
        int actorId = model.getActorId();
        int entityOwnerId = model.getEntityOwnerId();
        String timelineKey = RedisKeyUtil.getTimelineKey(actorId);
        jedisAdapter.lrme(timelineKey,entityOwnerId);
//        System.out.println("unfollow dohandle");
    }

    @Override
    public List<EventType> getSupportEventTypes()
    {
        return Arrays.asList(EventType.UNFOLLOW);
    }
}
