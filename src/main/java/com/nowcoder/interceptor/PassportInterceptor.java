/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: PassportInterceptor
 * Author:   LSN
 * Date:     2019/6/13 12:44
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.interceptor;

import com.nowcoder.dao.LoginTicketDao;
import com.nowcoder.dao.UserDao;
import com.nowcoder.model.HostHolder;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;

/**
 * 拦截器<br>
 * 〈用于跨用户界面判断是否登陆过〉
 *
 * @author LSN
 * @create 2019/6/13
 * @since 1.0.0
 */
@Component
public class PassportInterceptor implements HandlerInterceptor
{
    @Autowired
    LoginTicketDao loginTicketDao;

    @Autowired
    UserDao userDao;

    @Autowired
    HostHolder hostHolder;

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                             Object o) throws Exception
    {
        String ticket = null;
        if (httpServletRequest.getCookies() != null)
        {
            for (Cookie cookie : httpServletRequest.getCookies())
            {
                if (cookie.getName().equals("ticket"))
                {
                    ticket = cookie.getValue();
                    break;
                }
            }

            if (ticket != null)
            {
                LoginTicket loginTicket = loginTicketDao.selectByTicket(ticket);
                if (loginTicket == null || loginTicket.getExpired().before(new Date()) || loginTicket.getStatus() != 0)
                {
                    return true;
                }

                User user = userDao.selectById(loginTicket.getUserId());
                hostHolder.setUser(user);
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object o,
                           ModelAndView modelAndView) throws Exception
    {
        if (modelAndView != null)
        {
            modelAndView.addObject("user",hostHolder.getUser());
        }
    }

    @Override
    public void afterCompletion(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse,
                                Object o, Exception e) throws Exception
    {
        hostHolder.clear();
    }
}
