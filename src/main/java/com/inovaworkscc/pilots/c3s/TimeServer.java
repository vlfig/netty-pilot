package com.inovaworkscc.pilots.c3s;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ServerBootstrap;
import org.jboss.netty.channel.Channel;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.group.ChannelGroup;
import org.jboss.netty.channel.group.DefaultChannelGroup;
import org.jboss.netty.channel.socket.nio.NioServerSocketChannelFactory;

public class TimeServer extends SimpleChannelHandler {

	static final ChannelGroup allChannels = new DefaultChannelGroup("time-server");
	static final ChannelFactory factory = new NioServerSocketChannelFactory(
			Executors.newCachedThreadPool(),
			Executors.newCachedThreadPool());

	public static void main(String[] args) {
        final ServerBootstrap bootstrap = new ServerBootstrap(factory);

        // factory should be factored out
        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
			public ChannelPipeline getPipeline() {
                return Channels.pipeline(new TimeEncoder(), new TimeServerHandler());
            }
        });
        bootstrap.setOption("child.tcpNoDelay", true);
        bootstrap.setOption("child.keepAlive", true);

        final Channel channel = bootstrap.bind(new InetSocketAddress(8080));
        // first channel must be added here, no channelOpen method invoked on handler
        allChannels.add(channel);

        addShutdownHook();
	}

	private static void addShutdownHook() {
		Runtime.getRuntime().addShutdownHook(new Thread() {
			@Override
			public void run() {
				allChannels.close().awaitUninterruptibly();
				factory.releaseExternalResources();
				System.out.println("A graceful server shutdown. Jolly decent.");
			}
		});
	}
}
