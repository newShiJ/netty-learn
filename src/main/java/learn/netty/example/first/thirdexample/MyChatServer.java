package learn.netty.example.first.thirdexample;

import learn.netty.example.first.common.TemplateCode;

/**
 * @Author: cmm
 * @Date: 19-4-23 下午10:08
 * @Version 1.0
 */
public class MyChatServer {
    public static void main(String[] args) {
        TemplateCode.templateServer(8080,new MyChatServerInitializer());
    }
}
