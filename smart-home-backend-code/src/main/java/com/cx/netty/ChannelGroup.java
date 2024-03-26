package com.cx.netty;

import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

public class ChannelGroup {
    public static DefaultChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
    public static DefaultChannelGroup webSocketChannelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
}
