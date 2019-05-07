package learn.netty.example.first.heartbeat;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;
import io.netty.handler.timeout.IdleStateEvent;
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
        NioEventLoopGroup bossGroup = new NioEventLoopGroup();
        NioEventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            serverBootstrap.group(bossGroup,workerGroup)
                    .channel(NioServerSocketChannel.class)
                    .handler(new LoggingHandler(LogLevel.INFO))
                    .childHandler(new MyServerInitializer());
            ChannelFuture channelFuture = serverBootstrap.bind(8080).sync();
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            bossGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }

    public static class MyServerInitializer extends ChannelInitializer<SocketChannel> {
        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ChannelPipeline pipeline = ch.pipeline();
            // netty 空闲检测的处理器
            pipeline.addLast(new IdleStateHandler(5,7,2, TimeUnit.SECONDS));
            pipeline.addLast(new MyServerHandler());
        }
    }

    public static class MyServerHandler extends ChannelInboundHandlerAdapter {
        @Override
        public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
            if(evt instanceof IdleStateEvent){
                IdleStateEvent event = (IdleStateEvent) evt;
                String evenType = null;

                switch (event.state()){
                    case READER_IDLE:
                        evenType = "READER_IDLE";
                        break;
                    case ALL_IDLE:
                        evenType = "ALL_IDLE";
                        break;
                    case WRITER_IDLE:
                        evenType = "WRITER_IDLE";
                        break;
                    default:
                        break;
                }

                System.out.println(ctx.channel().remoteAddress() + " Timeout Event:" + evenType);
                ctx.channel().close();
            }
        }
    }
}

















