package learn.netty.example.jdk;

import javax.swing.*;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * @Author: cmm
 * @Date: 19-4-5 下午12:34
 * @Version 1.0
 */
public class NioServer {
    private ServerSocketChannel serverSocketChannel;
    private Selector selector;

    public NioServer ()throws IOException {
        // 打开 server Socket Channel
        serverSocketChannel = ServerSocketChannel.open();
        // 配置为非阻塞
        serverSocketChannel.configureBlocking(false);
        // 绑定 server port
        serverSocketChannel.socket().bind(new InetSocketAddress(8080));
        // 创建 Selector
        selector = Selector.open();
        // 注册 server Socket Channel 到 Selector
        serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
        System.out.println("Server 启动完成");

        handlerKeys();
    }

    private void handlerKeys() throws IOException{
        for (;;){
            // 通过 selector 选择 Channel
            int selectNums = selector.select(30 * 1000L);
            if(selectNums == 0){
                continue;
            }
            System.out.println("选择 Channel 数量:"+selectNums);

            // 遍历可选择的 channel 的 SelectionKey 集合
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                iterator.remove(); //移除下面需要处理的key
                if(!key.isValid()){ //忽略无效的 key
                    continue;
                }
                hanlderKey(key);
            }
        }
    }

    private void hanlderKey(SelectionKey key) throws IOException{
        // 接受连接就绪
        if(key.isAcceptable()){

        }
        // 读就绪
        if(key.isReadable()){

        }
        // 写就绪
        if(key.isWritable()){

        }
    }

    private void handleAcceptableKey(SelectionKey key)throws IOException{
        // 接受 client socket Channel
        SelectableChannel channel = key.channel();
        SocketChannel clientSocketChannel  = ((ServerSocketChannel)channel).accept();
        // 配置非阻塞
        clientSocketChannel.configureBlocking(false);
        System.out.println("Get new Channel");
        // 注册 client socket Channel 到 Selector
        clientSocketChannel.register(selector,SelectionKey.OP_READ,new ArrayList<String>());
    }

    private void handleReadableKey(SelectionKey key)throws IOException{
        // Client Socket Channel
        SocketChannel clientSocketChannel = (SocketChannel) key.channel();
        // 读取数据
        ByteBuffer readBuffer = CodecUtil.read(clientSocketChannel);
        // 处理连接已经断开的情况
        if (readBuffer == null) {
            System.out.println("断开 Channel");
            clientSocketChannel.register(selector, 0);
            return;
        }
        // 打印数据
        if (readBuffer.position() > 0) {
            String content = CodecUtil.newString(readBuffer);
            System.out.println("读取数据：" + content);

            // 添加到响应队列
            List<String> responseQueue = (ArrayList<String>) key.attachment();
            responseQueue.add("响应：" + content);
            // 注册 Client Socket Channel 到 Selector
            clientSocketChannel.register(selector, SelectionKey.OP_WRITE, key.attachment());
        }
    }


    private void handleWritableKey(SelectionKey key) throws ClosedChannelException {
        // Client Socket Channel
        SocketChannel clientSocketChannel = (SocketChannel) key.channel();

        // 遍历响应队列
        List<String> responseQueue = (ArrayList<String>) key.attachment();
        for (String content : responseQueue) {
            // 打印数据
            System.out.println("写入数据：" + content);
            // 返回
            CodecUtil.write(clientSocketChannel, content);
        }
        responseQueue.clear();

        // 注册 Client Socket Channel 到 Selector
        clientSocketChannel.register(selector, SelectionKey.OP_READ, responseQueue);
    }

    public static void main(String[] args) throws IOException {
        NioServer server = new NioServer();
    }
}




















