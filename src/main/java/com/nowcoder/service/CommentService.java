/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: CommentService
 * Author:   LSN
 * Date:     2019/6/13 19:56
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.service;

import com.nowcoder.dao.CommentDao;
import com.nowcoder.model.Comment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/13
 * @since 1.0.0
 */
@Service
public class CommentService
{
    @Autowired
    CommentDao commentDao;

    @Autowired
    SensitiveService sensitiveService;

    public List<Comment> getCommentsByEntity(int entityId, int entityType)
    {
        return commentDao.selectCommentByEntity(entityId, entityType);
    }

    public int addComment(Comment comment)
    {
        comment.setContent(HtmlUtils.htmlEscape(comment.getContent()));
        comment.setContent(sensitiveService.filter(comment.getContent()));
        return commentDao.addComment(comment);
    }

    public int getCommentCount(int entityId, int entityType)
    {
        return commentDao.getCommentCount(entityId, entityType);
    }

    public Comment getCommentById(int commentId)
    {
        return commentDao.getCommentById(commentId);
    }

    public boolean deleteComment(int commentId)
    {
        return commentDao.updateStatus(commentId, 1) > 0;
    }
}
