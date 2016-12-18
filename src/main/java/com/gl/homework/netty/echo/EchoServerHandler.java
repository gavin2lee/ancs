package com.gl.homework.netty.echo;


import java.io.UnsupportedEncodingException;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

public class EchoServerHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws UnsupportedEncodingException { 
    	System.out.println(msg.getClass().getName());
    	System.out.println(ctx.channel().remoteAddress()+"->Server :"+ msg.toString());
        ctx.writeAndFlush(msg); 
        
        String resp = "Msg from Server";
        ctx.channel().writeAndFlush(Unpooled.copiedBuffer(resp.getBytes("UTF-8")));
    }
    
    

    @Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.flush();
	}



	@Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) { 
        cause.printStackTrace();
        ctx.close();
    }
}