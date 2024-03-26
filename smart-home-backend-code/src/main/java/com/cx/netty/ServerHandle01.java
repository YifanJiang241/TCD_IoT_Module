package com.cx.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cx.mapper.DataMapper;
import com.cx.pojo.ToMes;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.util.Date;

import static com.cx.netty.ChannelGroup.channelGroup;
import static com.cx.netty.ChannelGroup.webSocketChannelGroup;

@ChannelHandler.Sharable
@Component
public class ServerHandle01 extends SimpleChannelInboundHandler<TextWebSocketFrame> {
    @Autowired
    private DataMapper dataMapper;
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @Override//转发数据
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, TextWebSocketFrame s) throws Exception {
       JSONObject jsonObject = JSONObject.parseObject(s.text());
        ToMes toMes=JSON.parseObject(s.text(),ToMes.class);
        System.out.println(JSONObject.toJSONString(toMes));
        channelGroup.writeAndFlush(JSONObject.toJSONString(toMes));


    }

    @Override//handlerAdded表示一旦连接成功,第一个被执行,将channel加入到channel组
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        webSocketChannelGroup.add(channel);
    }

    @Override//断开连接,提示全部在线成员
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        Date date = new Date();
        webSocketChannelGroup.writeAndFlush(dateFormat.format(date) + ":" + "前端:" + channel.remoteAddress() + "下线了" + "\n");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Date date = new Date();
        System.out.println(dateFormat.format(date) + ":" + ctx.channel().remoteAddress() + "前端上线了");
    }

    @Override//channel处于不活跃状态,
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Date date = new Date();
        System.out.println(dateFormat.format(date) + ":" + ctx.channel().remoteAddress() + "前端下线了");
    }


    @Override//出现异常关闭通道
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }


}
