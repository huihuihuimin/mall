/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: EventConsumer
 * Author:   LSN
 * Date:     2019/6/14 15:52
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.async;

import com.alibaba.fastjson.JSON;
import com.nowcoder.utils.JedisAdapter;
import com.nowcoder.utils.RedisKeyUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈通过consumer来建立event和handler之间的关系
 *  其处理方法有点类似于异步消息队列〉
 *
 * @author LSN
 * @create 2019/6/14
 * @since 1.0.0
 */
@Service
public class EventConsumer implements InitializingBean, ApplicationContextAware
{
    private static final Logger logger = LoggerFactory.getLogger(EventConsumer.class);

    @Autowired
    JedisAdapter jedisAdapter;

    //事件和对应handler实现类的关系映射
    private Map<EventType, List<EventHandler>> config = new HashMap<>();

    private ApplicationContext applicationContext;

    @Override
    public void afterPropertiesSet() throws Exception
    {
        //找出当前系统中所有EventHandler的实现类，handler有很多，不同的事件需要调用多个handler才能完成
        Map<String, EventHandler> beans = applicationContext.getBeansOfType(EventHandler.class);
        if (beans != null)
        {
            for (Map.Entry<String, EventHandler> entry : beans.entrySet())
            {
                //取每个handler实现类所关注的事件集合
                List<EventType> eventTypes = entry.getValue().getSupportEventTypes();
                //向config中注册handler
                for (EventType type : eventTypes)
                {
                    //为每一类事件，建立一个处理表，如提交代码事件后，需要完成排名，运行结果返回，完成数增减（这些都叫handler）···
                    if (!config.containsKey(type))
                    {
                        config.put(type, new ArrayList<EventHandler>());
                    }
                    config.get(type).add(entry.getValue());
                }
            }
        }
        //启动异步线程来从redis的队列中进行事件的获取，并处理
        //后期可以在此添加线程池，保证异步并发执行的速度
        Thread thread = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                while (true)
                {
                    String key = RedisKeyUtil.getEventQueueKey();
                    List<String> events = jedisAdapter.brpop(0, key);
                    for (String message : events)
                    {
                        //过滤第一返回值，因为为key不是事件
                        if (message.equals(key))
                        {
                            continue;
                        }
                        EventModel eventModel = JSON.parseObject(message, EventModel.class);
                        if (!config.containsKey(eventModel.getType()))
                        {
                            logger.error("不能识别的事件");
                            continue;
                        }
                        //识别事件，并从注册的事件handler映射表中取出对应的handler进行处理
                        for (EventHandler handler : config.get(eventModel.getType()))
                        {
                            handler.doHandle(eventModel);
                        }
                    }

                }
            }
        });
        thread.start();
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException
    {
        this.applicationContext = applicationContext;
    }
}
