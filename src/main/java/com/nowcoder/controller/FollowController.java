/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: FollowController
 * Author:   LSN
 * Date:     2019/6/16 13:17
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.model.*;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.FollowService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import com.nowcoder.utils.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/16
 * @since 1.0.0
 */
@Controller
public class FollowController
{
    @Autowired
    FollowService followService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    CommentService commentService;

    @RequestMapping(value = {"/followUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String setFollow(@RequestParam(value = "userId") int userId)
    {
        if (hostHolder.getUser() == null)
            return WendaUtil.getJSONString(999);

        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                                        .setEntityId(userId)
                                        .setEntityOwnerId(userId)
                                        .setEntityType(EntityType.ENTITY_USER)
                                        .setActorId(hostHolder.getUser().getId()));
        return WendaUtil.getJSONString(ret ? 0 : 1,
                                       String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),
                                                                                     EntityType.ENTITY_USER)));
    }

    @RequestMapping(value = {"/unfollowUser"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unFollow(@RequestParam(value = "userId") int userId)
    {
        if (hostHolder.getUser() == null)
            return WendaUtil.getJSONString(999);
        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_USER, userId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                                        .setEntityId(userId)
                                        .setEntityOwnerId(userId)
                                        .setEntityType(EntityType.ENTITY_USER)
                                        .setActorId(hostHolder.getUser().getId()));
        return WendaUtil.getJSONString(ret ? 0 : 1,
                                       String.valueOf(followService.getFolloweeCount(hostHolder.getUser().getId(),
                                                                                     EntityType.ENTITY_USER)));
    }

    @RequestMapping(value = {"/followQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String followQuestion(@RequestParam(value = "entityId") int entityId)
    {
        if (hostHolder.getUser() == null)
            return WendaUtil.getJSONString(999);

        Question q = questionService.getQuestionById(entityId);
        if (q == null)
            return WendaUtil.getJSONString(1, "问题不存在");

        boolean ret = followService.follow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, entityId);
        eventProducer.fireEvent(new EventModel(EventType.FOLLOW)
                                        .setEntityId(entityId)
                                        .setEntityOwnerId(q.getUserId())
                                        .setEntityType(EntityType.ENTITY_QUESTION)
                                        .setActorId(hostHolder.getUser().getId()));
        return setMap(entityId, ret);
    }

    @RequestMapping(value = {"/unfollowQuestion"}, method = {RequestMethod.POST})
    @ResponseBody
    public String unFollowQuestion(@RequestParam(value = "entityId") int entityId)
    {
        if (hostHolder.getUser() == null)
            return WendaUtil.getJSONString(999);

        Question q = questionService.getQuestionById(entityId);
        if (q == null)
            return WendaUtil.getJSONString(1, "问题不存在");

        boolean ret = followService.unfollow(hostHolder.getUser().getId(), EntityType.ENTITY_QUESTION, entityId);
        eventProducer.fireEvent(new EventModel(EventType.UNFOLLOW)
                                        .setEntityId(entityId)
                                        .setEntityOwnerId(q.getUserId())
                                        .setEntityType(EntityType.ENTITY_QUESTION)
                                        .setActorId(hostHolder.getUser().getId()));
        return setMap(entityId, ret);
    }

    private String setMap(int entityId, boolean ret)
    {
        Map<String, Object> info = new HashMap<>();
        info.put("headUrl", hostHolder.getUser().getHeadUrl());
        info.put("name", hostHolder.getUser().getName());
        info.put("id", hostHolder.getUser().getId());
        info.put("count", followService.getFollowerCount(EntityType.ENTITY_QUESTION, entityId));

        return WendaUtil.getJSONString(ret ? 0 : 1, info);
    }

    @RequestMapping(path = {"/user/{uid}/followees"}, method = {RequestMethod.GET})
    public String followees(Model model, @PathVariable("uid") int userId) {
        List<Integer> followeeIds = followService.getFollowees(userId,EntityType.ENTITY_USER,0,10);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followees", getUsersInfo(hostHolder.getUser().getId(), followeeIds));
        } else {
            model.addAttribute("followees", getUsersInfo(0, followeeIds));
        }
        model.addAttribute("followeeCount", followService.getFolloweeCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followers";
    }

    @RequestMapping(path = {"/user/{uid}/followers"}, method = {RequestMethod.GET})
    public String followers(Model model, @PathVariable("uid") int userId) {
        List<Integer> followerIds = followService.getFollowers(EntityType.ENTITY_USER, userId, 0, 10);
        if (hostHolder.getUser() != null) {
            model.addAttribute("followers", getUsersInfo(hostHolder.getUser().getId(), followerIds));
        } else {
            model.addAttribute("followers", getUsersInfo(0, followerIds));
        }
        model.addAttribute("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, userId));
        model.addAttribute("curUser", userService.getUser(userId));
        return "followers";
    }

    private List<ViewObject> getUsersInfo(int localUserId, List<Integer> userIds) {
        List<ViewObject> userInfos = new ArrayList<ViewObject>();
        for (Integer uid : userIds) {
            User user = userService.getUser(uid);
            if (user == null) {
                continue;
            }
            ViewObject vo = new ViewObject();
            vo.set("user", user);
            vo.set("commentCount", commentService.getUserCommentCount(uid));
            vo.set("followerCount", followService.getFollowerCount(EntityType.ENTITY_USER, uid));
            vo.set("followeeCount", followService.getFolloweeCount(uid, EntityType.ENTITY_USER));
            if (localUserId != 0) {
                vo.set("followed", followService.isFollower(localUserId, EntityType.ENTITY_USER, uid));
            } else {
                vo.set("followed", false);
            }
            userInfos.add(vo);
        }
        return userInfos;
    }

}
