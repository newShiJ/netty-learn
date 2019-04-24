package learn.netty.example.jdk;

import java.io.IOException;
import java.nio.channels.Channel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * @Author: cmm
 * @Date: 19-4-5 下午12:19
 * @Version 1.0
 */
public class JdkSelectorExample {

    public static void main(String[] args) throws Exception {
        //创建 Selector
        Selector selector = Selector.open();
        ServerSocketChannel channel = null;
        //将 Selector 注册到 Channel
        channel.configureBlocking(false);
        SelectionKey key = channel.register(selector, SelectionKey.OP_READ);
        while (true){
            //通过 Selector 选择 Channel
            int readyChannels = selector.select();
            if(readyChannels == 0){
                continue;
            }
            //获取可操作的 Channel
            Set<SelectionKey> keys = selector.selectedKeys();
            Iterator<SelectionKey> keyIterator = keys.iterator();
            while (keyIterator.hasNext()){
                SelectionKey selectionKey = keyIterator.next();
                if(selectionKey.isAcceptable()){

                }else if(selectionKey.isConnectable()){

                }else if (selectionKey.isReadable()){

                }else if (selectionKey.isWritable()){

                }
                keyIterator.remove();
            }
        }
    }
}
