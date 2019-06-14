/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: JedisAdapter
 * Author:   LSN
 * Date:     2019/6/14 12:26
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/14
 * @since 1.0.0
 */
@Service
public class JedisAdapter implements InitializingBean
{
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);

    private JedisPool jedisPool;

    @Override
    public void afterPropertiesSet() throws Exception
    {
        jedisPool = new JedisPool("redis://localhost:6379/10");
    }

    /**
     * 增加
     * @param key
     * @param value
     * @return
     */
    public long sadd(String key, String value)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            return jedis.sadd(key, value);
        } catch (Exception e)
        {
            logger.error("设置失败" + e.getMessage());
        }
        finally
        {
            if (jedis!=null)
                jedis.close();
        }
        return 0;
    }

    /**
     * 删除
     * @param key
     * @param value
     * @return
     */
    public long srem(String key, String value)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e)
        {
            logger.error("设置失败" + e.getMessage());
        }
        finally
        {
            if (jedis!=null)
                jedis.close();
        }
        return 0;
    }

    /**
     * 查询是否存在
     * @param key
     * @param value
     * @return
     */
    public boolean sismember(String key, String value)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            return jedis.sismember(key, value);
        } catch (Exception e)
        {
            logger.error("设置失败" + e.getMessage());
        }
        finally
        {
            if (jedis!=null)
                jedis.close();
        }
        return false;
    }

    /**
     * 查询数量
     * @param key
     * @return
     */
    public long scard(String key)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            return jedis.scard(key);
        } catch (Exception e)
        {
            logger.error("设置失败" + e.getMessage());
        }
        finally
        {
            if (jedis!=null)
                jedis.close();
        }
        return 0;
    }

    public long lpush(String key, String json)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            return jedis.lpush(key, json);
        } catch (Exception e)
        {
            logger.error("设置失败" + e.getMessage());
        }
        finally
        {
            if (jedis!=null)
                jedis.close();
        }
        return 0;
    }

    public List<String> brpop(int time,String key)
    {
        Jedis jedis = null;
        try
        {
            jedis = jedisPool.getResource();
            return jedis.brpop(0, key);
        } catch (Exception e)
        {
            logger.error("设置失败" + e.getMessage());
        }
        finally
        {
            if (jedis!=null)
                jedis.close();
        }
        return null;
    }

    public static void print(int index, Object obj)
    {
        System.out.println(String.format("%d,%s", index, obj.toString()));
    }

    public static void main(String[] args)
    {
        Jedis jedis = new Jedis("redis://localhost:6379/9");
        jedis.flushDB();

        jedis.set("hello", "world");
        print(1, jedis.get("hello"));
    }


}
