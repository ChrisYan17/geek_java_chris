package com.geek.chris.study.week4;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.IntStream;

public class ChrisThreadMain {

    public static void main(String[] args) {
        /**
         * 1.继承Thread
         */
        ChrisThread chrisThread = new ChrisThread();
        chrisThread.start();

        /**
         * 2.实现Runnable
         */
        new Thread( () -> System.out.println("2-我是new Thread(new Runnable())"+Thread.currentThread().getName()) ).start();

        /**
         * 3.使用Executors提供的静态方法创建线程池
         * 同new ThreadPoolExecutor 创建线程池
         * 提交一个Runnable
         */
        Executors.newSingleThreadExecutor().submit( () -> System.out.println("3-我是Executors.newSingleThreadExecutor()"+Thread.currentThread().getName()));

        /**
         * 3.使用Executors提供的静态方法创建线程池
         * 同new ThreadPoolExecutor 创建线程池
         * 提交一个Runnable
         *//*
        Future<String> future = Executors.newSingleThreadExecutor().submit(new Runnable() {
            @Override
            public void run() {
                System.out.println("4-我是Executors.newSingleThreadExecutor() Runnable String "+Thread.currentThread().getName());
            }
        }, "哈哈哈");
        try {
            System.out.println("4-我是Executors.newSingleThreadExecutor() Runnable String result="+future.get());
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }*/

        /**
         * 4.实现Callable
         */
        Callable callable = new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                System.out.println("4-我是new Callable<Integer>()"+Thread.currentThread().getName());
                return 0;
            }
        };
        FutureTask futureTask = new FutureTask(callable);
        new Thread(futureTask).start();

        /**
         * 5.实现Callable
         * 使用Executors提供的静态方法创建线程池
         * 同new ThreadPoolExecutor 创建线程池
         * 提交一个callable
         */
        Executors.newSingleThreadExecutor().submit(callable);

        /**
         * 6.使用parallelStream并行流
         */
        List<Integer> testList = new ArrayList<>();
        IntStream.range(1, 10).forEach(i -> testList.add(i));
        testList.parallelStream().forEach(i-> System.out.println(Thread.currentThread().getName()));

    }

}
