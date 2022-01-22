package com.geek.chris.study.week3;

import com.geek.chris.study.week3.filter.FullHttpRequestFilter;
import com.geek.chris.study.week3.filter.WorkFullHttpResponseFilter1;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.util.ReferenceCountUtil;

import java.util.ArrayList;
import java.util.List;

public class HttpChannelInboundHandler extends ChannelInboundHandlerAdapter {

    public List<FullHttpRequestFilter> fullHttpRequestFilters = new ArrayList<>();

    public HttpChannelOutboundHandler httpChannelOutboundHandler;

    private List<String> remoteUrls;

    public HttpChannelInboundHandler(List<String> remoteUrls) {
        this.remoteUrls = remoteUrls;
        httpChannelOutboundHandler = new HttpChannelOutboundHandler();
        httpChannelOutboundHandler.doAddResponseFilter(new WorkFullHttpResponseFilter1());
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    public void doAddReqestFilter(FullHttpRequestFilter fullHttpRequestFilter) {
        fullHttpRequestFilters.add(fullHttpRequestFilter);
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            System.out.println("HttpChannelInboundHandler流量接口请求开始，时间为：" + System.currentTimeMillis());
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
            String uri = fullHttpRequest.uri();
            System.out.println("HttpChannelInboundHandler接收到的请求url为：" + uri);
            //请求过滤
            if (doFilter(fullHttpRequest)) {
                return;
            }
            //OutInboundHandler
            httpChannelOutboundHandler.doHandler(ctx, fullHttpRequest, remoteUrls);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private boolean doFilter(FullHttpRequest fullHttpRequest) {
        for (FullHttpRequestFilter fullHttpRequestFilter : fullHttpRequestFilters) {
            if (fullHttpRequestFilter.doFilter(fullHttpRequest)) {
                System.out.println("被FullHttpRequestFilter拦截了");
                return true;
            }
        }
        return false;
    }
}
