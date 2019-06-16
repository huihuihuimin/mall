package com.nowcoder.service;

import com.nowcoder.dao.QuestionDao;
import com.nowcoder.model.Question;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.util.HtmlUtils;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/14
 * @since 1.0.0
 */
@Service
public class QuestionService
{
    @Autowired
    QuestionDao questionDao;

    @Autowired
    SensitiveService sensitiveService;

    public int addQuestion(Question question)
    {
        //使用工具类将文本中的html标签全部过滤掉，防止注入攻击(html过滤)
        question.setContent(HtmlUtils.htmlEscape(question.getContent()));
        question.setTitle(HtmlUtils.htmlEscape(question.getTitle()));
        //敏感词过滤
        question.setTitle(sensitiveService.filter(question.getTitle()));
        question.setContent(sensitiveService.filter(question.getContent()));

        return questionDao.addQuestion(question) > 0 ? question.getId() : 0;
    }

    public List<Question> getLatestQuestions(int userId, int offset, int limit)
    {
        return questionDao.selectLatestQuestions(userId, offset, limit);
    }

    public Question selectQuestionById(int qid)
    {
        return questionDao.selectQuestionById(qid);
    }

    public int updateCommentCount(int entityId, int count)
    {
        return questionDao.updateCommentCount(entityId,count);
    }

    public Question getQuestionById(int entityId)
    {
        return questionDao.getQuestionById(entityId);
    }
}
