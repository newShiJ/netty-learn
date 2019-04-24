package learn.netty.example.first.heartbit;

import io.netty.channel.*;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.timeout.IdleStateHandler;
import learn.netty.example.first.common.TemplateCode;

import java.util.concurrent.TimeUnit;

/**
 * @Author: cmm
 * @Date: 19-4-23 下午11:31
 * @Version 1.0
 */
public class MyServer {
    public static void main(String[] args) {
        TemplateCode.templateServer(8080,new MyServerInitializer());
    }

    public static class MyServerInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            // netty 空闲检测的处理器
            pipeline.addLast(new IdleStateHandler(5,7,10, TimeUnit.SECONDS));
        }
    }

    public static class MyServerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

        }
    }
}