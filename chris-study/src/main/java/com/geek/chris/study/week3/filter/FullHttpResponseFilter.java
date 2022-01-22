package com.geek.chris.study.week3.filter;

import io.netty.handler.codec.http.FullHttpResponse;

public interface FullHttpResponseFilter {

    public boolean doFilter(FullHttpResponse fullHttpResponse);
}
