/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: SearchController
 * Author:   LSN
 * Date:     2019/6/20 16:13
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.controller;

import com.nowcoder.model.EntityType;
import com.nowcoder.model.Question;
import com.nowcoder.model.ViewObject;
import com.nowcoder.service.FollowService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.SearchService;
import com.nowcoder.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/20
 * @since 1.0.0
 */
@Controller
public class SearchController
{
    private static final Logger logger = LoggerFactory.getLogger(SearchController.class);
    @Autowired
    SearchService searchService;

    @Autowired
    FollowService followService;

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @RequestMapping(path = {"/search"},method = {RequestMethod.GET})
    public String search(Model model,
                       @RequestParam("q") String keyword,
                       @RequestParam(value = "offset",defaultValue = "0") int offset,
                       @RequestParam(value = "count",defaultValue = "10")int count)
    {
        try
        {
            List<Question> questionList  = searchService.searchQuestion(keyword,offset,count,"<em>","</em>");
            List<ViewObject> vos = new ArrayList<>();
            for(Question question : questionList)
            {
                Question q = questionService.getQuestionById(question.getId());
                ViewObject vo = new ViewObject();
                if (question.getContent()!=null)
                {
                    q.setContent((question.getContent()));
                }
                if (question.getTitle() != null)
                {
                    q.setTitle((question.getTitle()));
                }
                vo.set("question",question);
                vo.set("user",userService.getUser(q.getUserId()));
                vo.set("followCount",followService.getFollowerCount(EntityType.ENTITY_QUESTION,question.getId()));
                vos.add(vo);
            }
            model.addAttribute("vos",vos);
            model.addAttribute("keyword",keyword);
        } catch (Exception e)
        {
            logger.error("失败"+e.getMessage());
        }
        return "result";
    }
}
