package learn.netty.example.protobuf.first;

import com.google.protobuf.InvalidProtocolBufferException;

/**
 * @author chenmingming
 * @date 2019/7/18
 */
public class FirstTest {
    public static void main(String[] args) throws InvalidProtocolBufferException {
        DataInfo.Student.Builder builder = DataInfo.Student.newBuilder();
        DataInfo.Student student = builder.setAddress("杭州")
                .setAge(24)
                .setName("名称")
                .build();
        byte[] bytes = student.toByteArray();
        DataInfo.Student parse = DataInfo.Student.parseFrom(bytes);
        System.out.println(parse);
        System.out.println(parse.getName());
        System.out.println(parse.getAddress());
    }
}
