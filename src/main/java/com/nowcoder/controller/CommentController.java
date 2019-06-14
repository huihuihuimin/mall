/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: CommentController
 * Author:   LSN
 * Date:     2019/6/13 20:04
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.controller;

import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.utils.WendaUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/13
 * @since 1.0.0
 */
@Controller
public class CommentController
{
    private static final Logger logger = LoggerFactory.getLogger(CommentController.class);
    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @Autowired
    HostHolder hostHolder;

    @RequestMapping(value = {"/addComment"}, method = {RequestMethod.POST})
    public String addComment(@RequestParam(value = "content") String content,
                             @RequestParam(value = "questionId") int questionId)
    {
        try
        {
            Comment comment = new Comment();
            comment.setContent(content);
            if (hostHolder.getUser() != null)
                comment.setUserId(hostHolder.getUser().getId());
            else
                comment.setUserId(WendaUtil.ANONYMOUS_USERID);
            comment.setCreatedDate(new Date());
            comment.setEntityType(EntityType.ENTITY_QUESTION);
            comment.setEntityId(questionId);
            commentService.addComment(comment);
            int count = commentService.getCommentCount(comment.getEntityId(),comment.getEntityType());
            questionService.updateCommentCount(comment.getEntityId(),count);
        } catch (Exception e)
        {
            logger.error("添加异常" + e.getMessage());
        }
        return "redirect:/question/" + questionId;
    }
}
