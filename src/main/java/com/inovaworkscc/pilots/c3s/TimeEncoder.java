package com.inovaworkscc.pilots.c3s;

import static org.jboss.netty.buffer.ChannelBuffers.buffer;

import org.jboss.netty.buffer.ChannelBuffer;
import org.jboss.netty.channel.ChannelHandlerContext;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.MessageEvent;
import org.jboss.netty.channel.SimpleChannelHandler;

public class TimeEncoder extends SimpleChannelHandler {

	@Override
	public void writeRequested(ChannelHandlerContext ctx, MessageEvent e) {
		final UnixTime t = (UnixTime) e.getMessage();
		final ChannelBuffer buf = buffer(4);
		buf.writeInt((int)t.getValue());
		Channels.write(ctx, e.getFuture(), buf);
	}
}
