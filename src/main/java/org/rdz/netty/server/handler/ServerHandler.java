package org.rdz.netty.server.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.rdz.common.entity.User;
import org.rdz.common.struct.RPCStruct;
import org.rdz.provider.UserServiceImpl;

@Slf4j
public class ServerHandler extends SimpleChannelInboundHandler<RPCStruct> {
    @Override
    public void channelRead0(ChannelHandlerContext ctx, RPCStruct msg) throws Exception {
        log.info("{},客户端发送消息:{}", Thread.currentThread().getName(), msg);
        //TODO:根据传过来来的信息分别调用某某类的某某方法
        ctx.writeAndFlush(msg.setResult(new UserServiceImpl().getUsers(null)));
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }
}
