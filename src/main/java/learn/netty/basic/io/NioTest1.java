package learn.netty.basic.io;

import learn.netty.rpc.thrift.first.PersonService;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.channels.spi.SelectorProvider;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;
import java.util.UUID;

/**
 * @Author: cmm
 * @Date: 19-8-2 下午5:34
 * @Version 1.0
 */
public class NioTest1 {

    @Test
    public void t1() throws Exception {
        FileInputStream inputStream = new FileInputStream("input.txt");
        FileOutputStream outputStream = new FileOutputStream("output.txt");


        FileChannel inputChannel = inputStream.getChannel();
        FileChannel outputChannel = outputStream.getChannel();

        ByteBuffer buffer = ByteBuffer.allocate(1024);

        while (true) {
            //buffer.clear();
            int read = inputChannel.read(buffer);
            System.out.println(read);
            if (read == -1) {
                break;
            }
            buffer.flip();
            outputChannel.write(buffer);
        }

        inputChannel.close();
        outputChannel.close();
    }

    @Test
    public void t2() throws Exception {
        ByteBuffer buffer = ByteBuffer.allocate(1024);
        buffer.putInt(992999991);
        buffer.putLong(150000L);
        buffer.putShort((short) 5);
        buffer.putChar('我');

        buffer.flip();

        System.out.println(buffer.getChar());
        System.out.println(buffer.getLong());
        System.out.println(buffer.getChar());
    }


    /***
     * 原来的buffer与slice方法生成的buffer共享底层数组，通过偏移量计算去处理达到目的
     */
    @Test
    public void t3() {
        ByteBuffer buffer = ByteBuffer.allocate(10);
        for (byte i = 0; i < buffer.capacity(); i++) {
            buffer.put(i);
        }

        buffer.position(2);
        buffer.limit(6);

        ByteBuffer slice = buffer.slice();
        for (int i = 0; i < slice.capacity(); i++) {
            byte b = slice.get(i);
            b *= 2;
            slice.put(i, b);
        }

        buffer.limit(buffer.capacity());
        for (int i = 0; i < buffer.capacity(); i++) {
            System.out.println(buffer.get(i));
        }


    }

    /**
     * net
     *
     * @throws Exception
     */
    @Test
    public void t4() throws Exception {
        int[] ports = new int[5];
        ports[0] = 5000;
        ports[1] = 5001;
        ports[2] = 5002;
        ports[3] = 5003;
        ports[4] = 5004;

        Selector selector = Selector.open();
        for (int i = 0; i < ports.length; i++) {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
            serverSocketChannel.configureBlocking(false);
            ServerSocket serverSocket = serverSocketChannel.socket();
            serverSocket.bind(new InetSocketAddress(ports[i]));

            serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
            System.out.println("listen:" + ports[i]);
        }
        while (true) {
            int nums = selector.select();
            System.out.println("get count" + nums);
            Set<SelectionKey> keySet = selector.selectedKeys();
            Iterator<SelectionKey> iterator = keySet.iterator();
            while (iterator.hasNext()) {
                SelectionKey next = iterator.next();
                if (next.isAcceptable()) {
                    ServerSocketChannel channel = (ServerSocketChannel) next.channel();
                    SocketChannel socketChannel = channel.accept();
                    socketChannel.configureBlocking(false);

                    socketChannel.register(selector, SelectionKey.OP_READ);
                    iterator.remove();
                    System.out.println("get client connection:" + socketChannel);
                }else if(next.isReadable()){
                    SocketChannel channel = (SocketChannel) next.channel();
                    int readSet =0;
                    while (true){
                        ByteBuffer buffer = ByteBuffer.allocate(512);
                        buffer.clear();
                        int  read = channel.read(buffer);
                        if(read <= 0){
                            break;
                        }
                        buffer.flip();
                        channel.write(buffer);
                        readSet += read;
                    }
                    System.out.println("read " + readSet + " from:"+ channel);
                    iterator.remove();
                }
            }
        }
    }

     
    @Test
    public void t5() throws Exception{
        HashMap<String, SocketChannel> clientMap = new HashMap<>();

        ServerSocketChannel serverSocketChannel = ServerSocketChannel.open();
        serverSocketChannel.bind(new InetSocketAddress(8899));
        Selector selector = Selector.open();
        ServerSocket socket = serverSocketChannel.socket();
        serverSocketChannel.configureBlocking(false);
        serverSocketChannel.register(selector,SelectionKey.OP_ACCEPT);
        for(;;){
            int select = selector.select();
            Set<SelectionKey> selectionKeys = selector.selectedKeys();
            Iterator<SelectionKey> iterator = selectionKeys.iterator();
            while (iterator.hasNext()){
                SelectionKey key = iterator.next();
                if(key.isAcceptable()){
                    ServerSocketChannel serverSocketCh = (ServerSocketChannel) key.channel();
                    SocketChannel client = serverSocketCh.accept();
                    client.configureBlocking(false);
                    client.register(selector,SelectionKey.OP_READ);
                    String clientKey = UUID.randomUUID().toString();
                    iterator.remove();
                    clientMap.put(clientKey,client);
                }else if (key.isReadable()){
                    SocketChannel client = (SocketChannel) key.channel();
                    ByteBuffer buffer = ByteBuffer.allocate(1024);
                    int read = client.read(buffer);
                    if(read > 0){
                        buffer.flip();
                        String msg = String.valueOf(Charset.forName("utf-8").decode(buffer).array());
                        msg =  client + ":" + msg;
                        System.out.println(msg);
                        buffer.clear();
                        ByteBuffer write = Charset.forName("utf-8").encode(msg);
                        clientMap.forEach((k,v) -> {
                            try {
                                v.write(write);
                                write.flip();
                            }catch (Exception ex){
                                ex.printStackTrace();
                            }
                        });
                        client.register(selector,SelectionKey.OP_READ);
                        iterator.remove();
                    }

                }
            }
        }
    }
}







































