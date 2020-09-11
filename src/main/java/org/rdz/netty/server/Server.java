package org.rdz.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import lombok.extern.slf4j.Slf4j;
import org.rdz.common.exception.RPCException;
import org.rdz.netty.server.initializer.ServerInitializer;

@Slf4j
public class Server {
    private static final String LOCAL_HOST = "localhost";
    private static final int DEFAULT_PORT = 8080;

    public static void main(String[] args) {
        Server server = new Server();
        server.startServer();
    }

    public void startServer() {
        startServer0(LOCAL_HOST, DEFAULT_PORT);
    }

    public void startServer(int port) {
        startServer0(LOCAL_HOST, port);
    }

    public void startServer(String host, int port) {
        startServer0(host, port);
    }

    /**
     * 开启服务端
     * @param host
     * @param port
     */
    private void startServer0(String host, int port) {
        NioEventLoopGroup bossGroup = new NioEventLoopGroup(1);      //专门负责网络接收
        NioEventLoopGroup workerGroup = new NioEventLoopGroup(8);    //专门负责网络读写

        try {
            //创建服务器端启动对象，配置启动参数
            ServerBootstrap server = new ServerBootstrap();
            //链式配置
            server.group(bossGroup, workerGroup)                   //设置两个线程组
                    .channel(NioServerSocketChannel.class)                  //使用NioServerSocketChannel作为服务器通道实现
                    .option(ChannelOption.SO_BACKLOG, 128)            //设置线程队列等待连接个数，初始化服务器可连接大小
                    .childOption(ChannelOption.SO_KEEPALIVE, true)    //设置保持活动连接状态
                    .childHandler(new ServerInitializer());

            ChannelFuture cf = server.bind(port).sync();
            cf.addListener(future -> {
                if(future.isSuccess()) {
                    log.info("服务端启动成功,端口{}", port);
                } else {
                    throw new RPCException("服务端启动失败");
                }
            });

            //对关闭通道进行监听
            cf.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}