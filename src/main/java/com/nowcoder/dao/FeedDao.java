/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: FeedDao
 * Author:   LSN
 * Date:     2019/6/17 16:33
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.dao;

import com.nowcoder.model.Comment;
import com.nowcoder.model.Feed;
import javafx.scene.control.Tab;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/17
 * @since 1.0.0
 */
@Mapper
public interface FeedDao
{
    String TABLE_NAME = " feed ";
    String INSERT_FIELDS = " user_id, data, created_date, type ";
    String SELECT_FIELDS = " id, " + INSERT_FIELDS;

    @Insert({"insert into", TABLE_NAME, "(", INSERT_FIELDS, ") values (  #{userId}, #{data}, " +
            " #{createdDate}, #{type} )"})
    int addFeed(Feed feed);

    /**
     * 推模式，根据id取内容
     * @param id 表示所好友的id
     * @return
     */
    @Select({"select ", SELECT_FIELDS," from ",TABLE_NAME," where id=#{id}"})
    Feed getFeedById(int id);

    /**
     * 拉模式
     * @param maxId 表示本页面最大id数，其余的id数都得小于这个数
     * @param userIds 表示登录状态下，更新的是所关注人的状态
     * @param count 表示分页
     * @return
     */
    List<Feed> selectUserFeeds(@Param("maxId") int maxId,
                               @Param("userIds") List<Integer> userIds,
                               @Param("count") int count);
}
