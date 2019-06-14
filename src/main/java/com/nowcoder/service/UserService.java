package com.nowcoder.service;

import com.nowcoder.dao.LoginTicketDao;
import com.nowcoder.dao.UserDao;
import com.nowcoder.model.LoginTicket;
import com.nowcoder.model.User;
import com.nowcoder.utils.WendaUtil;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/14
 * @since 1.0.0
 */
@Service
public class UserService
{
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
    @Autowired
    private UserDao userDao;

    @Autowired
    private LoginTicketDao loginTicketDao;

    public User getUser(int id)
    {
        return userDao.selectById(id);
    }

    public User selectByName(String name) {
        return userDao.selectByName(name);
    }

    public Map<String, String> register(String username, String password)
    {
        Map<String, String> map = new HashMap<>();
        map = checkNameAndPassword(username, password, map);
        if (map.containsKey("msg"))
            return map;
        User user = userDao.selectByName(username);
        if (user != null)
        {
            map.put("msg", "用户名已经存在");
            return map;
        }

        user = new User();
        user.setName(username);
        user.setSalt(UUID.randomUUID().toString().substring(0, 5));
        user.setHeadUrl(String.format("http://images.nowcoder.com/head/%dt.png", new Random().nextInt(1000)));
        user.setPassword(WendaUtil.MD5(password + user.getSalt()));
        userDao.addUser(user);
        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public Map<String, String> userLogin(String username, String password)
    {
        Map<String, String> map = new HashMap<>();
        map = checkNameAndPassword(username, password, map);
        if (map.containsKey("msg"))
            return map;
        User user = userDao.selectByName(username);
        if (user == null)
        {
            map.put("msg", "用户不存在");
            return map;
        }
        if (!StringUtils.equals(user.getPassword(), WendaUtil.MD5(password + user.getSalt())))
        {
            map.put("msg", "用户名或密码错误");
            return map;
        }

        String ticket = addLoginTicket(user.getId());
        map.put("ticket",ticket);
        return map;
    }

    public String addLoginTicket(int userId)
    {
        LoginTicket loginTicket = new LoginTicket();
        loginTicket.setUserId(userId);
        Date now = new Date();
        now.setTime(3600 * 24 * 100 + now.getTime());
        loginTicket.setExpired(now);
        loginTicket.setStatus(0);
        loginTicket.setTicket(UUID.randomUUID().toString().replaceAll("-", ""));
        loginTicketDao.addTicket(loginTicket);
        return loginTicket.getTicket();
    }

    private Map<String, String> checkNameAndPassword(String name, String password, Map<String, String> map)
    {
        if (StringUtils.isBlank(name))
        {
            map.put("msg", "用户名不能为空");
            return map;
        }
        if (StringUtils.isBlank(password))
        {
            map.put("msg", "密码不能为空");
            return map;
        }
        return map;
    }

    public void logout(String ticket)
    {
        loginTicketDao.updateStatus(ticket,1);
    }
}
