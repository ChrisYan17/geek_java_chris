package com.geek.chris.study.week3.filter;

import io.netty.handler.codec.http.FullHttpResponse;

public class WorkFullHttpResponseFilter1 implements FullHttpResponseFilter{
    @Override
    public boolean doFilter(FullHttpResponse fullHttpResponse) {

        System.out.println("我是WorkFullHttpResponseFilter1");

        return false;
    }
}
