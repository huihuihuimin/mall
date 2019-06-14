/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: EventType
 * Author:   LSN
 * Date:     2019/6/14 15:28
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.async;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/14
 * @since 1.0.0
 */
public enum EventType
{
    LIKE(0),
    COMMENT(1),
    LOGIN(2),
    MAIL(3);

    private int value;

    EventType(int value)
    {
        this.value = value;
    }

    public int getValue()
    {
        return value;
    }
}
