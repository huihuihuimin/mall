/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: AddQuestionHandler
 * Author:   LSN
 * Date:     2019/6/20 16:36
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.service.SearchService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/20
 * @since 1.0.0
 */
@Component
public class AddQuestionHandler implements EventHandler
{
    private static final Logger logger = LoggerFactory.getLogger(AddQuestionHandler.class);

    @Autowired
    SearchService searchService;

    @Override
    public void doHandle(EventModel model)
    {
        try
        {
            searchService.indexQuestion(model.getEntityId(),
                                        model.getExt("title"),
                                        model.getExt("content"));
        } catch (Exception e)
        {
            logger.error("增加失败" + e.getMessage());
        }
    }

    @Override
    public List<EventType> getSupportEventTypes()
    {
        return Arrays.asList(new EventType[]{EventType.ADD_QUESTION});
    }
}
