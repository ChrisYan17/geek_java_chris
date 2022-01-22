package com.geek.chris.study.week3;

import com.geek.chris.study.week2.GeekTestHttpSend;
import com.geek.chris.study.week3.filter.FullHttpResponseFilter;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.http.DefaultFullHttpResponse;
import io.netty.handler.codec.http.FullHttpRequest;
import io.netty.handler.codec.http.FullHttpResponse;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import static io.netty.handler.codec.http.HttpResponseStatus.OK;
import static io.netty.handler.codec.http.HttpVersion.HTTP_1_1;

public class HttpChannelOutboundHandler /*extends ChannelOutboundHandlerAdapter */{

    private List<FullHttpResponseFilter> fullHttpResponseFilters = new ArrayList<>();

    private ThreadPoolExecutor executors;

    HttpClient httpClient = HttpClients.createDefault();

    public HttpChannelOutboundHandler(){
        try{
            executors = new ThreadPoolExecutor(2,2,0L,TimeUnit.MILLISECONDS,new ArrayBlockingQueue<Runnable>(100),new DefaultThreadFactory("gk-netty-test"),new ThreadPoolExecutor.CallerRunsPolicy());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void doAddResponseFilter(FullHttpResponseFilter fullHttpResponseFilter){
        fullHttpResponseFilters.add(fullHttpResponseFilter);
    }

    public void doHandler(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest, List<String> outUrlList){
        String remoteUrl = routeUrl(outUrlList);

        doSend(ctx, fullHttpRequest, remoteUrl);

    }

    private String routeUrl(List<String> outUrlList){
        Random random = new Random(System.currentTimeMillis());
        return outUrlList.get(random.nextInt(outUrlList.size()));
    }

    private void doSend(ChannelHandlerContext ctx, FullHttpRequest fullHttpRequest,String remoteUrl){
        /*HttpGet httpGet = new HttpGet(remoteUrl);// 创建httpPost
        httpGet.addHeader("Content-type","application/json; charset=utf-8");
        httpGet.setHeader("Accept", "application/json");
        try {
            HttpResponse response = httpClient.execute(httpGet);
            byte[] body = EntityUtils.toByteArray(response.getEntity());
            FullHttpResponse fullHttpResponse  = new DefaultFullHttpResponse(HTTP_1_1, OK,Unpooled.wrappedBuffer(body));
            fullHttpResponse.headers().set("Content-Type", "application/json; charset=utf-8");
            fullHttpResponse.headers().setInt("Content-Length", Integer.parseInt(response.getFirstHeader("Content-Length").getValue()));
            //输出过滤
            doFilter(fullHttpResponse);
            ctx.write(fullHttpResponse);
        } catch (IOException e) {
            e.printStackTrace();
        }*/

        try{
            String respMsg = GeekTestHttpSend.doHttpSend(remoteUrl);
            byte[] msgBytes = respMsg.getBytes("utf-8");
            FullHttpResponse fullHttpResponse  = new DefaultFullHttpResponse(HTTP_1_1, OK,Unpooled.wrappedBuffer(msgBytes));
            fullHttpResponse.headers().set("Content-Type", "application/json; charset=utf-8");
            fullHttpResponse.headers().setInt("Content-Length", msgBytes.length);
            //输出过滤
            doFilter(fullHttpResponse);
            ctx.write(fullHttpResponse);
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }

    private boolean doFilter(FullHttpResponse fullHttpResponse){
        for(FullHttpResponseFilter fullHttpResponseFilter:fullHttpResponseFilters){
            if(fullHttpResponseFilter.doFilter(fullHttpResponse)){
                System.out.println("被FullHttpResponseFilter拦截了");
                return true;
            }
        }
        return false;
    }


}
