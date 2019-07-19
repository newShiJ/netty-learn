package learn.netty.example.protobuf.nettyFirst;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * @author chenmingming
 * @date 2019/7/18
 */
public class TestClient {
    public static void main(String[] args) throws Exception {
        NioEventLoopGroup eventLoopGroup = new NioEventLoopGroup();
        try {
            Bootstrap bootstrap = new Bootstrap();
            bootstrap.group(eventLoopGroup)
                    .channel(NioSocketChannel.class)
                    .handler(new TestClientInitializer());
            Channel channel = bootstrap.connect("localhost", 9999).sync().channel();
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            for (;;){
                channel.writeAndFlush(reader.readLine());
            }
        }finally {
            eventLoopGroup.shutdownGracefully();
        }
    }
}
