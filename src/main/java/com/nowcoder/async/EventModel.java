/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: EventModel
 * Author:   LSN
 * Date:     2019/6/14 15:30
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.async;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/14
 * @since 1.0.0
 */
public class EventModel implements Serializable
{
    private static final long serialVersionUID = 1L;
    //事件类型
    private EventType type;
    //触发者
    private int actorId;
    //触发载体
    private int entityType;
    private int entityId;
    //触发相关对象
    private int entityOwnerId;
    private Map<String, String> exts = new HashMap<>();

    public EventModel()
    {
    }

    public EventModel(EventType type)
    {
        this.type = type;
    }

    public EventType getType()
    {
        return type;
    }

    public EventModel setType(EventType type)
    {
        this.type = type;
        return this;
    }

    public int getActorId()
    {
        return actorId;
    }

    public EventModel setActorId(int actorId)
    {
        this.actorId = actorId;
        return this;
    }

    public int getEntityType()
    {
        return entityType;
    }

    public EventModel setEntityType(int entityType)
    {
        this.entityType = entityType;
        return this;
    }

    public int getEntityId()
    {
        return entityId;
    }

    public EventModel setEntityId(int entityId)
    {
        this.entityId = entityId;
        return this;
    }

    public int getEntityOwnerId()
    {
        return entityOwnerId;
    }

    public EventModel setEntityOwnerId(int entityOwnerId)
    {
        this.entityOwnerId = entityOwnerId;
        return this;
    }

    public String getExt(String key)
    {
        return exts.get(key);
    }

    public EventModel setExt(String key, String value) {
        exts.put(key, value);
        return this;
    }

    public EventModel setExts(Map<String, String> exts)
    {
        this.exts = exts;
        return this;
    }

    public Map<String, String> getExts() {
        return exts;
    }
}
