package learn.netty.example.first.httpDemo;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelPipeline;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpServerCodec;

/**
 * @Author: cmm
 * @Date: 19-4-21 上午11:47
 * @Version 1.0
 */
public class FirstServerInitalizer extends ChannelInitializer<SocketChannel> {
    protected void initChannel(SocketChannel ch) throws Exception {
        ChannelPipeline pipeline = ch.pipeline();
        pipeline.addLast("HttpServerCodec",new HttpServerCodec());
        pipeline.addLast("FirstHttpServerHandler",new FirstHttpServerHandler());
    }
}
