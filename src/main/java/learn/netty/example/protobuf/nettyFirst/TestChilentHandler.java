package learn.netty.example.protobuf.nettyFirst;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import learn.netty.example.protobuf.first.DataInfo;

/**
 * @author chenmingming
 * @date 2019/7/18
 */
public class TestChilentHandler extends SimpleChannelInboundHandler<DataInfo.Student> {

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, DataInfo.Student msg) throws Exception {

    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        DataInfo.Student student = DataInfo.Student.newBuilder()
                .setName("陈鸣铭")
                .setAge(24)
                .setAddress("HZ")
                .build();
        ctx.channel().writeAndFlush(student);
    }
}
