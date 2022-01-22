package com.geek.chris.study.week3.filter;

import io.netty.handler.codec.http.FullHttpRequest;

public interface FullHttpRequestFilter {

    public boolean doFilter(FullHttpRequest fullHttpRequest);
}
