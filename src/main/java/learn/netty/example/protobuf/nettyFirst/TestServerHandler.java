package learn.netty.example.protobuf.nettyFirst;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import learn.netty.example.protobuf.first.DataInfo;

/**
 * @author chenmingming
 * @date 2019/7/18
 */
public class TestServerHandler extends SimpleChannelInboundHandler<DataInfo.Student> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DataInfo.Student msg) throws Exception {
        System.out.println(msg);
    }
}
