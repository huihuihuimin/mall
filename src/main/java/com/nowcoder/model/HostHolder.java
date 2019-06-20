/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: HostHolder
 * Author:   LSN
 * Date:     2019/6/13 13:01
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.model;

import org.springframework.stereotype.Component;

import java.util.WeakHashMap;

/**
 * 〈一句话功能简述〉<br> 
 * 〈使用threadlocal为每个线程保存当前的用户信息，并将其注入spring让其代管〉
 *
 * @author LSN
 * @create 2019/6/13
 * @since 1.0.0
 */
@Component
public class HostHolder
{
    private static ThreadLocal<User> users = new ThreadLocal<>();

    public User getUser()
    {
        return users.get();
    }

    public void setUser(User user)
    {
        users.set(user);
    }

    public void clear()
    {
        users.remove();
    }
}
