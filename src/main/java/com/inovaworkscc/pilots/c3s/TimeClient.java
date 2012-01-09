package com.inovaworkscc.pilots.c3s;

import java.net.InetSocketAddress;
import java.util.concurrent.Executors;

import org.jboss.netty.bootstrap.ClientBootstrap;
import org.jboss.netty.channel.ChannelFactory;
import org.jboss.netty.channel.ChannelFuture;
import org.jboss.netty.channel.ChannelPipeline;
import org.jboss.netty.channel.ChannelPipelineFactory;
import org.jboss.netty.channel.Channels;
import org.jboss.netty.channel.SimpleChannelHandler;
import org.jboss.netty.channel.socket.nio.NioClientSocketChannelFactory;

public class TimeClient extends SimpleChannelHandler {

	public static void main(String[] args) {
        final String host = args[0];
        final int port = Integer.parseInt(args[1]);

        final ChannelFactory factory =
            new NioClientSocketChannelFactory(
                    Executors.newCachedThreadPool(),
                    Executors.newCachedThreadPool());

        final ClientBootstrap bootstrap = new ClientBootstrap(factory);

        bootstrap.setPipelineFactory(new ChannelPipelineFactory() {
            @Override
			public ChannelPipeline getPipeline() {
                return Channels.pipeline(new TimeDecoder(), new TimeClientHandler());
            }
        });
        bootstrap.setOption("tcpNoDelay", true);
        bootstrap.setOption("keepAlive", true);

        // try connect
        final ChannelFuture future = bootstrap.connect(new InetSocketAddress(host, port));
        future.awaitUninterruptibly();
        if(!future.isSuccess()) {
        	// connect failed
        	future.getCause().printStackTrace();
        }
        // sit on the channel's 'close future'
        future.getChannel().getCloseFuture().awaitUninterruptibly();
        // all connections close now:
        factory.releaseExternalResources();
        System.out.println("A graceful client shutdown. Jolly decent.");
	}
}
