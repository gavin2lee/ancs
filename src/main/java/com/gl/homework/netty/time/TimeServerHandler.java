package com.gl.homework.netty.time;

import java.util.Date;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class TimeServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		ByteBuf buf = (ByteBuf) msg;
		byte[] reqBytes = new byte[buf.readableBytes()];
		buf.readBytes(reqBytes);
		
		String reqMsg = new String(reqBytes,"UTF-8");
		System.out.println("server received:"+reqMsg);
		
		String curTime = new Date().toString();
		
		ByteBuf resp = Unpooled.copiedBuffer(curTime.getBytes("UTF-8"));
		ctx.write(resp);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}
	
}
