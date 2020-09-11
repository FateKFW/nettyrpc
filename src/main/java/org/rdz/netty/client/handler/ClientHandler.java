package org.rdz.netty.client.handler;

import io.netty.channel.SimpleChannelInboundHandler;
import org.rdz.common.struct.RPCStruct;

/**
 * RPC调用客户端处理
 */
public abstract class ClientHandler extends SimpleChannelInboundHandler<RPCStruct> {
    protected Object result;

    public abstract Object getResult(String msgid);
    public abstract void write2Remotely(String msgid, RPCStruct struct);
}
