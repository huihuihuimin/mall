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

import com.nowcoder.model.*;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
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
                return WendaUtil.getJSONString(0);

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
                                                          comment.getEntityId()));
            vo.set("likeCount", likeService.getLikeCount(EntityType.ENTITY_COMMENT, comment.getEntityId()));
            vo.set("user", userService.getUser(comment.getUserId()));
            comments.add(vo);
        }
        model.addAttribute("comments", comments);
        return "detail";
    }
}
