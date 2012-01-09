package com.inovaworkscc.pilots.c3s;

import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelFutureListener;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ChannelStateEvent;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class TimeServerHandler extends SimpleChannelHandler {

    @Override
    public void channelConnected(ChannelHandlerContext ctx, ChannelStateEvent e) {
    	int times = 0;
    	do {
    		e.getChannel().write(new UnixTime(System.currentTimeMillis() / 1000));
    	} while(++times < 20);

    	// we want the future of the last write
        final ChannelFuture cf = e.getChannel().write(new UnixTime(System.currentTimeMillis() / 1000));
        cf.addListener(ChannelFutureListener.CLOSE);
    }

    @Override
    public void channelOpen(ChannelHandlerContext ctx, ChannelStateEvent e) {
        TimeServer.allChannels.add(e.getChannel());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
