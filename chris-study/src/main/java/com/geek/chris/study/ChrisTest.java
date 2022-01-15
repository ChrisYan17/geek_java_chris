package com.geek.chris.study;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class ChrisTest {
    /*public static void main(String[] args) {
        System.out.println("123");
    }*/

    public static void main(String[] args) {
        ThreadPoolExecutor threadPool = new ThreadPoolExecutor(10, 10,
                0L, TimeUnit.MILLISECONDS,
                new LinkedBlockingQueue<>(10));
        //信号总数为5
        Semaphore semaphore = new Semaphore(5);
        //运行10个线程
        for (int i = 0; i < 10; i++) {
            int num = i;
            threadPool.execute(new Runnable() {

                @Override
                public void run() {
                    try {
                        //获取信号
                        semaphore.acquire();
                        System.out.println(Thread.currentThread().getName()+";i="+ num + ";获得了信号量,时间为" + System.currentTimeMillis());
                        //阻塞2秒，测试效果
                        Thread.sleep(2000);
                        System.out.println(Thread.currentThread().getName() + "释放了信号量,时间为" + System.currentTimeMillis());
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        //释放信号
                        semaphore.release();
                    }

                }
            });
        }
        threadPool.shutdown();
    }
}
