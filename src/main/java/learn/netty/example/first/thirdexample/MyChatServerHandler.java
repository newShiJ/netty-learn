package learn.netty.example.first.thirdexample;

import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

/**
 * @Author: cmm
 * @Date: 19-4-23 下午10:24
 * @Version 1.0
 */
public class MyChatServerHandler extends SimpleChannelInboundHandler<String> {

    private static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, String msg) throws Exception {
        Channel channel = ctx.channel();
//        channelGroup.forEach(ch -> {
//            if (channel != ch) {
//                ch.writeAndFlush(channel.remoteAddress() + ":" + msg + "\n");
//            } else {
//                channel.writeAndFlush("[me]:" + msg + "\n");
//            }
//        });
        channelGroup.writeAndFlush("[me]:" + msg + "\n",channel1 -> channel == channel1);
        channelGroup.writeAndFlush(channel.remoteAddress() + ":" + msg + "\n",channel1 -> channel != channel1);
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        cause.printStackTrace();
        ctx.close();
        super.exceptionCaught(ctx, cause);
    }

    @Override
    public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[server]: " + channel.remoteAddress() + "join\n");
        channelGroup.add(channel);
    }

    @Override
    public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush("[server]: " + channel.remoteAddress() + "remove\n");
        /**
         * 当netty发现连接关闭后因为这个GlobalEventExecutor是全局的
         * 所以netty会自动将其进行回收所以可以不写下面的这一行代码
         */
//        channelGroup.remove(channel);
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(channel.remoteAddress() + ":upLine\n");
    }


    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        Channel channel = ctx.channel();
        channelGroup.writeAndFlush(channel.remoteAddress() + ":downLine\n");
    }
}













