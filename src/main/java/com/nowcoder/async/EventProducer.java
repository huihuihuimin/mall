/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: EventProducer
 * Author:   LSN
 * Date:     2019/6/14 15:43
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.async;

import com.alibaba.fastjson.JSONObject;
import com.nowcoder.utils.JedisAdapter;
import com.nowcoder.utils.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.BlockingQueue;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/14
 * @since 1.0.0
 */
@Service
public class EventProducer
{
    @Autowired
    JedisAdapter jedisAdapter;

    public boolean fireEvent(EventModel eventModel)
    {
        try
        {
            String json = JSONObject.toJSONString(eventModel);
            String key = RedisKeyUtil.getEventQueueKey();
            jedisAdapter.lpush(key,json);
            return true;
        } catch (Exception e)
        {
            return false;
        }
    }
}
