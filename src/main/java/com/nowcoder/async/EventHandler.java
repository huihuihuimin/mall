/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: EventHandler
 * Author:   LSN
 * Date:     2019/6/14 15:53
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.async;

import com.nowcoder.model.EntityType;

import java.util.List;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/14
 * @since 1.0.0
 */
public interface EventHandler
{
    //对感兴趣的事件进行处理
    void doHandle(EventModel model);

    //注册自己对那些事件感兴趣
    List<EventType> getSupportEventTypes();
}
