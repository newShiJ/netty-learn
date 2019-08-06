package learn.netty.basic.io;

import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;

/**
 * @Author: cmm
 * @Date: 19-8-5 下午2:49
 * @Version 1.0
 */
public class NioMyTest {

    public static void main(String[] args) throws Exception{
        t2();
    }

    public static void t1()throws Exception{
        ReadableByteChannel source = Channels.newChannel(System.in);
        WritableByteChannel dest = Channels.newChannel(System.out);
        channelCopy2 (source, dest);
// alternatively, call channelCopy2 (source, dest);
        source.close( );
        dest.close( );
    }

    private static void channelCopy1 (ReadableByteChannel src,
                                      WritableByteChannel dest) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect(16 * 1024);
        while (src.read(buffer) != -1) {
            buffer.flip();
            dest.write(buffer);
            buffer.compact();
        }
    }

    private static void channelCopy2 (ReadableByteChannel src,
                                      WritableByteChannel dest) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocateDirect (16 * 1024);
        while (src.read (buffer) != -1) {
            buffer.flip( );
            while (buffer.hasRemaining( )) {
                dest.write (buffer);
            }
            buffer.clear( );
        }
    }

    public static void t2() throws Exception{
        File temp = File.createTempFile ("holy", null);
        RandomAccessFile file = new RandomAccessFile (temp, "rw");
        FileChannel channel = file.getChannel( );
// Create a working buffer
        ByteBuffer byteBuffer = ByteBuffer.allocateDirect (100);
        putData (0, byteBuffer, channel);
        putData (5000000, byteBuffer, channel);
        putData (50000, byteBuffer, channel);
        System.out.println ("Wrote temp file '" + temp.getPath( )
                + "', size=" + channel.size( ));
        channel.close( );
        file.close( );
    }

    private static void putData (int position, ByteBuffer buffer,
                                 FileChannel channel)
            throws IOException
    {
        String string = "*<-- location " + position;
        buffer.clear( );
        buffer.put (string.getBytes ("US-ASCII"));
        buffer.flip( );
        channel.position (position);
        channel.write (buffer);
    }
}
