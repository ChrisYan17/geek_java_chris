package com.geek.chris.study.week2.netty;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.http.*;
import io.netty.util.ReferenceCountUtil;

public class HttpHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
            System.out.println("ChannelRead流量接口请求开始，时间为：" + System.currentTimeMillis());
            FullHttpRequest fullHttpRequest = (FullHttpRequest) msg;
            String uri = fullHttpRequest.uri();
            System.out.println("接收到的请求url为：" + uri);
            if (uri.contains("/test")) {
                handlerTest(fullHttpRequest, ctx,"hello,chris");
            } else {
                handlerTest(fullHttpRequest, ctx,"hello,others");
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    private void handlerTest(FullHttpRequest fullHttpRequest, ChannelHandlerContext ctx, String value) {
        FullHttpResponse fullHttpResponse = null;
        try {

            fullHttpResponse = new DefaultFullHttpResponse(
                    HttpVersion.HTTP_1_1, HttpResponseStatus.OK, Unpooled.wrappedBuffer(value.getBytes("utf-8")));
            fullHttpResponse.headers().set("Content-Type", "application/json");
            fullHttpResponse.headers().set("Content-Length", fullHttpResponse.content().readableBytes());
        } catch (Exception e) {
            fullHttpResponse = new DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.NO_CONTENT);
            e.printStackTrace();
        } finally {
            if (null != fullHttpRequest) {
                if (!HttpUtil.isKeepAlive(fullHttpRequest)) {
                    ctx.write(fullHttpResponse).addListener(ChannelFutureListener.CLOSE);
                } else {
                    fullHttpResponse.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE);
                    ctx.write(fullHttpResponse);
                }
            }

        }
    }
}
