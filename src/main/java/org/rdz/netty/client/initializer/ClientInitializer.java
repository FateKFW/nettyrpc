package org.rdz.netty.client.initializer;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.handler.codec.serialization.ClassResolvers;
import io.netty.handler.codec.serialization.ObjectDecoder;
import io.netty.handler.codec.serialization.ObjectEncoder;
import io.netty.handler.codec.string.StringDecoder;
import io.netty.handler.codec.string.StringEncoder;

/**
 * handler职责链初始化
 */
public class ClientInitializer extends ChannelInitializer {
    private ChannelHandler[] handlers;

    public void setHandlers(ChannelHandler[] handlers) {
        this.handlers = handlers;
    }

    @Override
    protected void initChannel(Channel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("jdkencoder", new ObjectEncoder());
        pipeline.addLast("jdkdecoder", new ObjectDecoder(Integer.MAX_VALUE, ClassResolvers.cacheDisabled(null)));
        for (int i = 0; i < handlers.length; i++) {
            pipeline.addLast("CustomClientHandler"+i, handlers[i]);
        }
    }
}
