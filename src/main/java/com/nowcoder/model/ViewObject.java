package com.nowcoder.model;

import java.util.HashMap;
import java.util.Map;

/**
 * 〈一句话功能简述〉<br>
 * 〈用于将复杂信息返回至前端界面〉
 *
 * @author LSN
 * @create 2019/6/13
 * @since 1.0.0
 */
public class ViewObject
{
    private Map<String, Object> objs = new HashMap<String, Object>();

    public void set(String key, Object value)
    {
        objs.put(key, value);
    }

    public Object get(String key)
    {
        return objs.get(key);
    }
}
