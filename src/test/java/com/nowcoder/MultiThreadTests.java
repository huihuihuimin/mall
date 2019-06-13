/**
 * Copyright (C), 2015-2019, XXX有限公司
 * FileName: MultiThreadTests
 * Author:   LSN
 * Date:     2019/6/13 17:07
 * Description:
 * History:
 * <author>          <time>          <version>          <desc>
 * 作者姓名           修改时间           版本号              描述
 */
package com.nowcoder;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 〈一句话功能简述〉<br>
 * 〈〉
 *
 * @author LSN
 * @create 2019/6/13
 * @since 1.0.0
 */
class MyThread extends Thread
{
    private int tid;

    public MyThread(int tid)
    {
        this.tid = tid;
    }

    @Override
    public void run()
    {
        try
        {
            int sum = 0;
            while (sum < 10)
            {
                sum++;
                Thread.sleep(1000);
                System.out.println("tid:" + tid + "=====" + "sum:" + sum);
            }
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

class Consumer implements Runnable
{
    private BlockingQueue<String> q;

    public Consumer(BlockingQueue<String> q)
    {
        this.q = q;
    }

    @Override
    public void run()
    {
        try
        {
            while(true)
            {
                System.out.println(Thread.currentThread().getName()+":"+q.take());
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}


class Producer implements Runnable
{
    private BlockingQueue<String> q;

    public Producer(BlockingQueue<String> q)
    {
        this.q = q;
    }

    @Override
    public void run()
    {
        try
        {
            for(int i=0;i<100;i++)
            {
                Thread.sleep(10);
                q.put(String.valueOf(i));
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
}

public class MultiThreadTests
{
    private static Object obj = new Object();

    private static int id = 0;

    public static void testThread()
    {
        for (int i = 0; i < 10; i++)
            new MyThread(i).start();
    }

    public static void testSynchronized1(int j)
    {
        synchronized (obj)
        {
            try
            {
                for (int i = 0; i < 5; i++)
                {
                    Thread.sleep(1);
                    System.out.println(String.format("T3 %d:%d", j, i));
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void testSynchronized2(int j)
    {
        synchronized (obj)
        {
            try
            {
                for (int i = 0; i < 5; i++)
                {
                    Thread.sleep(1000);
                    System.out.println(String.format("T4 %d:%d", j, i));
                }
            } catch (Exception e)
            {
                e.printStackTrace();
            }
        }
    }

    public static void testSynchronized()
    {
        for (int i = 0; i < 2; i++)
        {
            new Thread(new Runnable()
            {
                @Override
                public void run()
                {
                    id++;
                    testSynchronized1(id);
                    testSynchronized2(id);
                }
            }).start();
        }
    }

    public static  void testBlockingQueue()
    {
        BlockingQueue<String> q = new ArrayBlockingQueue<String>(10);
        new Thread(new Producer(q)).start();
        new Thread(new Consumer(q),"Constumer1").start();
        new Thread(new Consumer(q),"Constumer2").start();
    }

    public static void main(String[] args)
    {
//        testThread();
//        testSynchronized();
        testBlockingQueue();
    }

}
