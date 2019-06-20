/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: QuestionController
 * Author:   LSN
 * Date:     2019/6/13 14:40
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
import com.nowcoder.service.*;
import com.nowcoder.utils.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/13
 * @since 1.0.0
 */
@Controller
public class QuestionController
{
    private static final Logger logger = LoggerFactory.getLogger(QuestionController.class);

    @Autowired
    QuestionService questionService;

    @Autowired
    UserService userService;

    @Autowired
    HostHolder hostHolder;

    @Autowired
    CommentService commentService;

    @Autowired
    LikeService likeService;

    @Autowired
    FollowService followService;

    @Autowired
    EventProducer eventProducer;

    @RequestMapping(value = {"/question/add"}, method = {RequestMethod.POST})
    @ResponseBody
    public String addQuestion(@RequestParam("title") String title, @RequestParam("content") String content)
    {
        try
        {
            Question question = new Question();
            question.setTitle(title);
            question.setContent(content);
            question.setCreatedDate(new Date());
            question.setCommentCount(0);
            if (hostHolder.getUser() == null)
//                question.setUserId(WendaUtil.getAnonymousUserid());
                return WendaUtil.getJSONString(999);
            else
                question.setUserId(hostHolder.getUser().getId());
            if (questionService.addQuestion(question) > 0)
            {
                eventProducer.fireEvent(new EventModel(EventType.ADD_QUESTION)
                                                .setActorId(question.getUserId())
                                                .setEntityId(question.getId()).setExt("title", question.getTitle())
                                                .setExt("content", question.getContent()));
                return WendaUtil.getJSONString(0);
            }
        } catch (Exception e)
        {
            logger.error("增加题目失败" + e.getMessage());
        }
        return WendaUtil.getJSONString(1, "失败");
    }

    @RequestMapping(value = "/question/{qid}")
    public String questionDetail(Model model, @PathVariable(value = "qid") int qid)
    {
        Question question = questionService.selectQuestionById(qid);
        model.addAttribute("question", question);

        List<Comment> commentList = commentService.getCommentsByEntity(qid, EntityType.ENTITY_QUESTION);
        int commentCount = commentService.getCommentCount(qid, EntityType.ENTITY_QUESTION);

        List<ViewObject> comments = new ArrayList<>();
        for (Comment comment : commentList)
        {
            ViewObject vo = new ViewObject();
            vo.set("comment", comment);
            if (hostHolder.getUser() == null)
                vo.set("liked", 0);
            else
                vo.set("liked", likeService.getLikeStatus(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT,
                                                          comment.getId()));
            vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getId()));
            vo.set("user", userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments", comments);

        List<ViewObject> followUsers = new ArrayList<ViewObject>();
        // 获取关注的用户信息
        List<Integer> users = followService.getFollowers(EntityType.ENTITY_QUESTION, qid, 20);
        for (Integer userId : users)
        {
            ViewObject vo = new ViewObject();
            User u = userService.getUser(userId);
            if (u == null)
            {
                continue;
            }
            vo.set("name", u.getName());
            vo.set("headUrl", u.getHeadUrl());
            vo.set("id", u.getId());
            followUsers.add(vo);
        }
        model.addAttribute("followUsers", followUsers);
        if (hostHolder.getUser() != null)
        {
            model.addAttribute("followed", followService.isFollower(hostHolder.getUser().getId(),
                                                                    EntityType.ENTITY_QUESTION, qid));
        } else
        {
            model.addAttribute("followed", false);
        }

        return "detail";
    }
}
