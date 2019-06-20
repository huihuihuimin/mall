/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: FeedService
 * Author:   LSN
 * Date:     2019/6/17 16:34
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.service;

import com.nowcoder.dao.FeedDao;
import com.nowcoder.model.Feed;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/17
 * @since 1.0.0
 */
@Service
public class FeedService
{
    @Autowired
    FeedDao feedDao;

    public List<Feed> getUserFeeds(int maxId, List<Integer> userIds, int count)
    {
        return feedDao.selectUserFeeds(maxId, userIds, count);
    }

    public boolean addFeed(Feed feed)
    {
        feedDao.addFeed(feed);
        return feed.getId() > 0;
    }

    public Feed getById(int id)
    {
        return feedDao.getFeedById(id);
    }
}
