package com.gl.homework.netty.time;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class TimeServer {
	public static final int PORT = 8089;

	public void bind(int port) throws Exception {
		EventLoopGroup bossGrp = new NioEventLoopGroup();
		EventLoopGroup workerGrp = new NioEventLoopGroup();

		try {
			ServerBootstrap server = new ServerBootstrap();
			server.group(bossGrp).channel(NioServerSocketChannel.class).option(ChannelOption.SO_BACKLOG, 1024)
					.childHandler(new ChildChannelHandler());
			
			ChannelFuture cf = server.bind(port).sync();
			
			cf.channel().closeFuture().sync();
		} finally {
			bossGrp.shutdownGracefully();
			workerGrp.shutdownGracefully();
		}
	}

	public static void main(String... args) throws Exception {
		int port = PORT;
		new TimeServer().bind(port);
	}
}
