package com.geek.chris.study.week3;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.Channel;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.epoll.EpollChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

import java.util.ArrayList;
import java.util.List;

public class WorkNettyHttpServer {

    public static List<String> doGetOutUrls(){
        List<String> outUrl = new ArrayList<>(3);
        outUrl.add("http://127.0.0.1:8801");
        outUrl.add("http://127.0.0.1:8802");
        return outUrl;
    }

    public static void main(String[] args) {

        int port = 8808;
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workerGroup = new NioEventLoopGroup(4);
        try {
            ServerBootstrap sb = new ServerBootstrap();
            //等待队列，指定了大小
            sb.option(ChannelOption.SO_BACKLOG, 128)
                    /**
                     * 此参数禁用Nagle算法，使用于小数据即时传输；
                     * Nagle算法，将小的数据包组装为更大的帧然后进行发送，包数量不足会等待其他数据，故效率高，但可能延迟，
                     * 与TCP_NODELAY相对应的是TCP_CORK，该选项是需要等到发送的数据量最大的时候，一次性发送，适用于文件传输
                     */
                    .childOption(ChannelOption.TCP_NODELAY, true)
                    /**
                     * 参数对应于套接字选项中的SO_KEEPALIVE，该参数用于设置TCP连接，
                     * 当设置该选项以后，连接会测试链接的状态，这个选项用于可能长时间没有数据交流的连接。
                     * 当设置该选项以后，如果在两小时内没有数据的通信时，TCP会自动发送一个活动探测数据报文。
                     */
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    /**
                     * 应于套接字选项中的SO_REUSEADDR，这个参数表示允许重复使用本地地址和端口，
                     *
                     * 比如，某个服务器进程占用了TCP的80端口进行监听，此时再次监听该端口就会返回错误，使用该参数就可以解决问题，该参数允许共用该端口，这个在服务器程序中比较常使用，
                     * 比如某个进程非正常退出，该程序占用的端口可能要被占用一段时间才能允许其他进程使用，而且程序死掉以后，内核一需要一定的时间才能够释放此端口，不设置SO_REUSEADDR就无法正常使用该端口
                     */
                    .childOption(ChannelOption.SO_REUSEADDR, true)
                    /**
                     * 接收缓冲区大小
                     */
                    .childOption(ChannelOption.SO_RCVBUF, 32 * 1024)
                    /**
                     * 发送缓冲区大小
                     */
                    .childOption(ChannelOption.SO_SNDBUF, 32 * 1024)
                    .childOption(EpollChannelOption.SO_REUSEPORT, true)
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);

            sb.group(bossGroup, workerGroup)//主从reactor
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.DEBUG))
                    .childHandler(new WorkHttpInitializer(doGetOutUrls()));
            Channel ch = sb.bind(port).sync().channel();
            System.out.println("开启netty http服务器，监听地址和端口为 http://127.0.0.1:" + port);
            ch.closeFuture().sync();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
