package com.cx.netty;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cx.mapper.DataMapper;
import com.cx.pojo.*;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import net.sf.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.text.SimpleDateFormat;
import java.util.Date;
import static com.cx.netty.ChannelGroup.channelGroup;
import static com.cx.netty.ChannelGroup.webSocketChannelGroup;

@ChannelHandler.Sharable
@Component
public class ServerHandle extends SimpleChannelInboundHandler<Object> {
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat dateFormat01 = new SimpleDateFormat("HH:mm:ss");
    @Autowired
    private DataMapper dataMapper;

    @Override
    protected void messageReceived(ChannelHandlerContext channelHandlerContext, Object s) throws Exception {
        System.out.println(s.toString());
        JSONObject jsonObject = JSONObject.parseObject(String.valueOf(s));
        Date date = new Date();
        String time = dateFormat.format(date);
        System.out.println(jsonObject.toString());
        DeviceMes deviceMes = JSON.parseObject(s.toString(), DeviceMes.class);
        deviceMes.setNowTime(time);
        deviceMes.setNowTimeDate(date);
        //存储数据
        int i = dataMapper.insertDeviceMes(deviceMes);
        if(i>0){
            webSocketChannelGroup.writeAndFlush(new TextWebSocketFrame(JSON.toJSONString(deviceMes)));
        }
    }


    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.add(channel);

    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "硬件已断开连接");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        System.out.println(channel.remoteAddress() + "硬件已连接");
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        System.out.println(ctx.channel().remoteAddress() + "硬件将要断开连接");
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}
