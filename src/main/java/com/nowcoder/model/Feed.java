/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: Feed
 * Author:   LSN
 * Date:     2019/6/17 16:33
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.model;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;

import java.util.Date;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/17
 * @since 1.0.0
 */
@Component
public class Feed
{
    private int id;
    private int type;
    private int userId;
    private Date createdDate;
    //使用json格式来对data进行存储，因为每种不同类型的新鲜事，内容是不同的
    private String data;
    //辅助变量，因为data中的内容是不定的，因为不同的feed对应着不同的内容，那么通过feed来取数据是不现实的（data中可能存在feed中没有定义的属性）
    //因此，没有办法通过在feed中添加"get属性名()"的方式来获取内容，因此增加了此辅助变量，通过json数据解析为k-v的格式，来进行统一的get
    private JSONObject dataJSON = null;

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public int getType()
    {
        return type;
    }

    public void setType(int type)
    {
        this.type = type;
    }

    public int getUserId()
    {
        return userId;
    }

    public void setUserId(int userId)
    {
        this.userId = userId;
    }

    public Date getCreatedDate()
    {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate)
    {
        this.createdDate = createdDate;
    }

    public String getData()
    {
        return data;
    }

    public void setData(String data)
    {
        this.data = data;
        dataJSON = JSONObject.parseObject(data);
    }

    public String get(String key)
    {
        return dataJSON == null ? null : dataJSON.getString(key);
    }

}
