/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: LoginTicket
 * Author:   LSN
 * Date:     2019/6/13 9:45
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.model;

import java.util.Date;

/**
 * 〈一句话功能简述〉<br> 
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/13
 * @since 1.0.0
 */
public class LoginTicket
{
    private int id;
    private int userId;
    private String ticket;
    private Date expired;
    private int status;

    public LoginTicket()
    {
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public String getTicket()
    {
        return ticket;
    }

    public void setTicket(String ticket)
    {
        this.ticket = ticket;
    }

    public Date getExpired()
    {
        return expired;
    }

    public void setExpired(Date expired)
    {
        this.expired = expired;
    }

    public int getStatus()
    {
        return status;
    }

    public void setStatus(int status)
    {
        this.status = status;
    }
}
