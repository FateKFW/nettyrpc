package org.rdz.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.rdz.common.exception.RPCException;
import org.rdz.common.struct.RPCStruct;
import org.rdz.netty.client.handler.ClientHandler;
import org.rdz.netty.client.handler.DefaultClientHandler;
import org.rdz.netty.client.initializer.ClientInitializer;
import org.rdz.netty.client.inter.RequestIDCreator;

import java.lang.reflect.Proxy;
import java.util.UUID;

/**
 * RPC调用客户端
 */
@Slf4j
public class Client {
    private static final String LOCAL_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;
    //处理器链
    private ClientInitializer initializer;
    //客户端调用处理器
    private ClientHandler clientHandler;
    //是否初始化过参数
    private boolean isInit = false;
    //与服务器连接是否成功
    private volatile boolean starting = true;
    //唯一消息ID生成方式
    private RequestIDCreator requestIDCreator;

    public void setInitializer(ClientInitializer initializer) {
        this.isInit = true;
        this.initializer = initializer;
    }

    public void setClientHandler(ClientHandler clientHandler) {
        this.isInit = true;
        this.clientHandler = clientHandler;
    }

    public void setRequestIDCreator(RequestIDCreator requestIDCreator) {
        this.requestIDCreator = requestIDCreator;
    }

    public void startClient() {
        if(!isInit) initDefaultParam();
        new Thread(() -> startClient0(LOCAL_HOST, DEFAULT_PORT)).start();
    }

    public void startClient(int port) {
        if(!isInit) initDefaultParam();
        new Thread(() -> startClient0(LOCAL_HOST, port)).start();
    }

    public void startClient(String host, int port) {
        if(!isInit) initDefaultParam();
        new Thread(() -> startClient0(host, port)).start();
    }

    private void initDefaultParam() {
        this.initializer = new ClientInitializer();
        this.clientHandler = new DefaultClientHandler();
        this.initializer.setHandlers(new ChannelHandler[]{clientHandler});
        this.requestIDCreator = struct -> UUID.randomUUID().toString().replace("-", "");
        this.isInit = true;
    }

    /**
     * 开启客户端
     * @param host
     * @param port
     */
    private void startClient0(String host, int port) {
        NioEventLoopGroup group = null;
        Bootstrap client;
        try {
            group = new NioEventLoopGroup();
            client = new Bootstrap();
            client.group(group)
                    .channel(NioSocketChannel.class)
                    .option(ChannelOption.TCP_NODELAY, true)
                    .handler(initializer);
            ChannelFuture clientChannelFuture = client.connect(host, port).sync();
            clientChannelFuture.addListener(future -> {
                if(future.isSuccess()) {
                    starting = false;
                } else {
                    throw new RPCException("连接服务端失败");
                }
            });
            //给关闭通道增加一个监听
            clientChannelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            group.shutdownGracefully();
        }
    }

    /**
     * 调用远程方法
     * @param struct
     * @param msgid
     */
    private Object callMethod(RPCStruct struct, String msgid) {
        while (starting) {} //自旋等待连接
        clientHandler.write2Remotely(msgid, struct);
        return clientHandler.getResult(msgid);
    }

    /**
     * 获取调用代理对象
     * @param clazz
     * @return
     */
    public Object getBean(final Class<?> clazz) {
        return Proxy.newProxyInstance(clazz.getClassLoader(), new Class<?>[]{clazz}, (proxy, method, args) -> {
            RPCStruct struct = new RPCStruct();
            struct.setClassName(clazz.getName());
            struct.setMethodName(method.getName());
            struct.setParameterType(method.getParameterTypes());
            struct.setParameterValues(args);
            return callMethod(struct, requestIDCreator.getRequestID(struct));
        });
    }
}
