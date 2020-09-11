package org.rdz.netty.client.handler;

import io.netty.channel.ChannelHandlerContext;
import org.rdz.common.struct.RPCStruct;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 * 默认处理实现
 */
public class DefaultClientHandler extends ClientHandler {
    //服务器是否回传处理结果
    private BlockingQueue<Boolean> canRead = new ArrayBlockingQueue<>(1);
    //调用上下文
    private ChannelHandlerContext context;

    /**
     * 在连接建立的时候初始化上下午呢
     * @param ctx
     * @throws Exception
     */
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        this.context = ctx;
    }

    /**
     * RPC调用回调
     * @param ctx
     * @param msg
     * @throws Exception
     */
    @Override
    public void channelRead0(ChannelHandlerContext ctx, RPCStruct msg) throws Exception {
        result = msg.getResult();
        canRead.add(true);
    }

    /**
     * 异常处理
     * @param ctx
     * @param cause
     * @throws Exception
     */
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
    }

    /**
     * 获取调用结果
     * @param msgid
     * @return
     */
    @Override
    public Object getResult(String msgid) {
        try {
            canRead.take();
        } catch (InterruptedException e) {
            e.printStackTrace();
            result = null;
        }
        return result;
    }

    /**
     * 发送调用请求
     * @param msgid
     * @param struct
     */
    @Override
    public void write2Remotely(String msgid, RPCStruct struct) {
        struct.setRequestID(msgid);
        this.context.writeAndFlush(struct);
    }
}
