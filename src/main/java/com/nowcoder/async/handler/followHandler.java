/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: followHandler
 * Author:   LSN
 * Date:     2019/6/16 14:02
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.async.handler;

import com.nowcoder.async.EventHandler;
import com.nowcoder.async.EventModel;
import com.nowcoder.async.EventType;
import com.nowcoder.model.EntityType;
import com.nowcoder.model.Message;
import com.nowcoder.model.User;
import com.nowcoder.service.MessageService;
import com.nowcoder.service.QuestionService;
import com.nowcoder.service.UserService;
import com.nowcoder.utils.WendaUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/16
 * @since 1.0.0
 */
@Component
public class followHandler implements EventHandler
{
    @Autowired
    MessageService messageService;

    @Autowired
    UserService userService;

    @Autowired
    QuestionService questionService;

    @Override
    public void doHandle(EventModel model)
    {
        Message message = new Message();
        message.setFromId(WendaUtil.SYSTEM_USERID);
        message.setToId(model.getEntityOwnerId());
        message.setCreatedDate(new Date());
        User user = userService.getUser(model.getActorId());
        if (model.getEntityType() == EntityType.ENTITY_QUESTION)
            message.setContent("用户" + user.getName() + "评论了你，http://127.0.0.1:8080/question" + model.getEntityId());
        else if(model.getEntityType() == EntityType.ENTITY_USER)
            message.setContent("用户" + user.getName() + "关注了你，http://127.0.0.1:8080/user" + model.getActorId());
        messageService.addMessage(message);
    }

    @Override
    public List<EventType> getSupportEventTypes()
    {
        return Arrays.asList(EventType.FOLLOW);
    }
}
