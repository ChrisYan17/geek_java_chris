package com.geek.chris.study.week3.filter;

import io.netty.handler.codec.http.FullHttpRequest;

public class WorkFullHttpRequestFilter1 implements FullHttpRequestFilter{
    @Override
    public boolean doFilter(FullHttpRequest fullHttpRequest) {

        System.out.println("我是WorkFullHttpRequestFilter1");

        return false;
    }
}
