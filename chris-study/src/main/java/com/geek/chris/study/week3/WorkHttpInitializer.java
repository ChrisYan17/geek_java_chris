package com.geek.chris.study.week3;

import com.geek.chris.study.week3.filter.WorkFullHttpRequestFilter1;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;

import java.util.List;

public class WorkHttpInitializer extends ChannelInitializer<SocketChannel> {

    private List<String> remoteUrls;

    public WorkHttpInitializer(List<String> remoteUrls){
        this.remoteUrls = remoteUrls;
    }

    @Override
    protected void initChannel(SocketChannel socketChannel) {
        ChannelPipeline p = socketChannel.pipeline();
        p.addLast(new HttpServerCodec());
        //p.addLast(new HttpServerExpectContinueHandler());
        p.addLast(new HttpObjectAggregator(1024*1024));

        HttpChannelInboundHandler httpChannelInboundHandler = new HttpChannelInboundHandler(remoteUrls);
        httpChannelInboundHandler.doAddReqestFilter(new WorkFullHttpRequestFilter1());

        p.addLast(httpChannelInboundHandler);
    }
}
