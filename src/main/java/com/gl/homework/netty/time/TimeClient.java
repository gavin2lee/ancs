package com.gl.homework.netty.time;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

public class TimeClient {
	public void connect(int port, String host) throws Exception{
		EventLoopGroup grp = new NioEventLoopGroup();
		try{
			Bootstrap client = new Bootstrap();
			client.group(grp)
			.channel(NioSocketChannel.class)
			.option(ChannelOption.TCP_NODELAY, true)
			.handler(new ChannelInitializer<SocketChannel>() {

				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new TimeClientHandler());
				}	
				
			});
			
			
			ChannelFuture cf = client.connect(host, port).sync();
			
			cf.channel().closeFuture().sync();
		}finally{
			grp.shutdownGracefully();
		}
	}
	
	public static void main(String...args) throws Exception{
		new TimeClient().connect(TimeServer.PORT, "localhost");
	}
}
