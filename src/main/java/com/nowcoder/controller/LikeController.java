/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: LikeController
 * Author:   LSN
 * Date:     2019/6/14 14:13
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.controller;

import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventProducer;
import com.nowcoder.async.EventType;
import com.nowcoder.model.Comment;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.Question;
import com.nowcoder.service.CommentService;
import com.nowcoder.service.LikeService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.utils.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 〈一句话功能简述〉<br>
 * 〈用于点赞操作〉
 *
 * @author LSN
 * @create 2019/6/14
 * @since 1.0.0
 */
@Controller
public class LikeController
{
    @Autowired
    HostHolder hostHolder;

    @Autowired
    LikeService likeService;

    @Autowired
    EventProducer eventProducer;

    @Autowired
    CommentService commentService;

    @Autowired
    QuestionService questionService;

    @RequestMapping(value = {"/like"}, method = {RequestMethod.POST})
    @ResponseBody
    public String like(@RequestParam("commentId") int commentId)
    {
        if (hostHolder.getUser() == null)
        {
            return "redirect:/reglogin";
        }
        Comment comment = commentService.getCommentById(commentId);
        eventProducer.fireEvent(new EventModel(EventType.LIKE)
                                        .setActorId(hostHolder.getUser().getId())
                                        .setEntityType(EntityType.ENTITY_COMMENT)
                                        .setEntityId(commentId)
                                        .setEntityOwnerId(comment.getUserId())
                                        .setExt("questionId", String.valueOf(comment.getEntityId())));

        long likeCount = likeService.like(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);

        return WendaUtil.getJSONString(0, String.valueOf(likeCount));
    }

    @RequestMapping(value = {"/dislike"}, method = {RequestMethod.POST})
    @ResponseBody
    public String dislike(@RequestParam("commentId") int commentId)
    {
        if (hostHolder.getUser() == null)
        {
            return "redirect:/reglogin";
        }
        long dislikeCount = likeService.disLike(hostHolder.getUser().getId(), EntityType.ENTITY_COMMENT, commentId);

        return WendaUtil.getJSONString(0, String.valueOf(dislikeCount));
    }
}
