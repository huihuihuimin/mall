/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: FeedController
 * Author:   LSN
 * Date:     2019/6/17 16:33
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.controller;

import com.nowcoder.model.EntityType;
import com.nowcoder.model.Feed;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.FeedService;
import com.nowcoder.service.FollowService;
import com.nowcoder.utils.JedisAdapter;
import com.nowcoder.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/17
 * @since 1.0.0
 */
@Controller
public class FeedController
{
    @Autowired
    FeedService feedService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    FollowService followService;

    @Autowired
    JedisAdapter jedisAdapter;

    /**
     * 拉模式
     * 不使用timeline，而是用户登录后，直接查询关注列表
     * 根据列表中的id，在feed流表中查询与id相关的feed流并返回
     * 读取压力大
     * @param model
     * @return
     */
    @RequestMapping(path = {"/pullfeeds"}, method = {RequestMethod.GET})
    private String getPullFeeds(Model model)
    {
        int localUserId = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();
        //关注列表
        List<Integer> followees = new ArrayList<>();
        if (localUserId != 0)
        {
            //获取当前用户的关注列表
            followees = followService.getFollowees(localUserId, EntityType.ENTITY_USER, Integer.MAX_VALUE);
        }
        //通过关注列表获取列表中所有关注ID的feed流并返回，即用户上线后，查询自己所关注人/问题的所有动态
        List<Feed> feeds = feedService.getUserFeeds(Integer.MAX_VALUE, followees, 10);
        model.addAttribute("feeds", feeds);
        return "feeds";
    }

    /**
     *  推模式
     *  事件触发后，广播给所有粉丝，即为所有的粉丝生成该事件的timeline，但feed流只有一个；
     *  粉丝上线后，只需要查询timeline然后对应获取有事件触发的关注对象的feed流即可
     *  后台压力大
     * @param model
     * @return
     */
    @RequestMapping(path = {"/pushfeeds"}, method = {RequestMethod.GET})
    private String getPushFeeds(Model model)
    {
        int localUserId = hostHolder.getUser() == null ? 0 : hostHolder.getUser().getId();
        //在timeline中查找与当前用户相关的feedId
        List<String> feedIds = jedisAdapter.lrange(RedisKeyUtil.getTimelineKey(localUserId), 0, 10);
//        System.out.println("localuserId=" + localUserId + "|||||feedsize=" + feedIds.size());
        List<Feed> feeds = new ArrayList<>();
        for (String feedId : feedIds)
        {
//            System.out.println("feedId=" + feedId);
            //通过feedid获取对应id的feed流，并显示
            Feed feed = feedService.getById(Integer.parseInt(feedId));
//            System.out.println("feed=" + feed);
            if (feed == null)
                continue;
            feeds.add(feed);
        }
        model.addAttribute("feeds", feeds);
        return "feeds";
    }
}
