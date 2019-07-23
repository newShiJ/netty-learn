package learn.netty.example.protobuf.nettyFirst;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import learn.netty.example.protobuf.first.DataInfo;

import java.util.Random;

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
//        DataInfo.Student student = DataInfo.Student.newBuilder()
//                .setName("陈鸣铭")
//                .setAge(24)
//                .setAddress("HZ")
//                .build();
        int i = new Random().nextInt(4);
        MyDataInfo.MyMessage.Builder builder = MyDataInfo.MyMessage.newBuilder();
        switch (i){
            case 0:{
                builder.setDataType(MyDataInfo.MyMessage.DataType.CatType);
                MyDataInfo.Cat.Builder tempBuilder = MyDataInfo.Cat.newBuilder()
                        .setAddress("HZ")
                        .setName("CAT");
                builder.setCat(tempBuilder);
                break;
            }
            case 1:{
                builder.setDataType(MyDataInfo.MyMessage.DataType.DogType);
                MyDataInfo.Dog.Builder tempBuilder = MyDataInfo.Dog.newBuilder()
                        .setName("DOG")
                        .setAge(123);
                builder.setDog(tempBuilder);
                break;
            }
            default:{
                builder.setDataType(MyDataInfo.MyMessage.DataType.PersonType);
                MyDataInfo.Person.Builder tempBuilder = MyDataInfo.Person.newBuilder()
                        .setName("PERSON")
                        .setAge(123)
                        .setAddress("HZ");
                builder.setPerson(tempBuilder);
                break;
            }
        }
        ctx.channel().writeAndFlush(builder.build());
    }
}
