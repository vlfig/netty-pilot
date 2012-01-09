package com.inovaworkscc.pilots.c3s;

import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.ExceptionEvent;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class TimeClientHandler extends SimpleChannelHandler {

	private int times = 0;

	@Override
	public void messageReceived(ChannelHandlerContext ctx, MessageEvent e) {
		final UnixTime time = (UnixTime) e.getMessage();
		times++;
		System.out.println("The time is " + time + " for the " + times + " time.");
		if (times > 2) {
			System.out.println("Received third (or more) time, closing");
			e.getChannel().close();
		}
	}

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, ExceptionEvent e) {
        e.getCause().printStackTrace();
        e.getChannel().close();
    }
}
