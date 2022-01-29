package com.geek.chris.study.week4;

public class ChrisThread extends Thread{

    @Override
    public void run() {
        System.out.println("1-我是ChrisThread"+Thread.currentThread().getName());
    }
}
