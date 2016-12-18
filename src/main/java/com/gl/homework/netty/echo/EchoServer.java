
package com.gl.homework.netty.echo;

import java.nio.charset.Charset;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;
import io.netty.handler.codec.Delimiters;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

public class EchoServer {

	private int port;

	public EchoServer(int port) {
		this.port = port;
	}

	public void run() throws Exception {
		EventLoopGroup bossGroup = new NioEventLoopGroup();
		EventLoopGroup workerGroup = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class)
					.childHandler(new ChannelInitializer<SocketChannel>() {
						@Override
						public void initChannel(SocketChannel ch) throws Exception {
							ch.pipeline().addLast("framer",
									new DelimiterBasedFrameDecoder(8192, Delimiters.lineDelimiter()));
							ch.pipeline().addLast("decoder", new StringDecoder(Charset.forName("UTF-8")));
							ch.pipeline().addLast("encoder", new StringEncoder(Charset.forName("UTF-8")));
							ch.pipeline().addLast(new EchoServerHandler());
						}
					}).option(ChannelOption.SO_BACKLOG, 1024).childOption(ChannelOption.SO_KEEPALIVE, true);
			ChannelFuture f = b.bind(port).sync();

			System.out.println("Server start listen at " + port);

			f.channel().closeFuture().sync();
		} finally {
			workerGroup.shutdownGracefully();
			bossGroup.shutdownGracefully();
		}
	}

	public static void main(String[] args) throws Exception {
		int port;
		if (args.length > 0) {
			port = Integer.parseInt(args[0]);
		} else {
			port = 8088;
		}
		new EchoServer(port).run();
	}
}