package net.gudenau.jgecko.natives;

import java.io.FileDescriptor;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

/**
 * The native methods as used on Linux.
 *
 * Nothing is really documented because Man pages
 * and I am lazy.
 * */
public class Linux{
    static{
        Loader.loadNatives();
    }
    
    public static final int F_SETFL   = 4;
    
    public static final int CRTSCTS   = 0x80000000;
    public static final int CS8       = 0x00000030;
    public static final int CLOCAL    = 0x00000800;
    public static final int CREAD     = 0x00000080;
    
    public static final int TCSANOW   = 0;
    public static final int TCIOFLUSH = 2;
    
    public static int getRealFD(FileDescriptor descriptor){
        return doGetRealFD(Objects.requireNonNull(descriptor));
    }
    
    public static int tcgetattr(int fd, termios termios_p){
        termios_p.buffer = ByteBuffer.allocateDirect(getTermiosSize());
        termios_p.buffer.order(ByteOrder.nativeOrder());
        int result = tcgetattr(fd, getBufferAddress(termios_p.buffer));
        termios_p.buffer.order(ByteOrder.nativeOrder());
        nativeToJava(termios_p);
        return result;
    }
    
    public static void cfmakeraw(termios termios_p){
        javaToNative(termios_p);
        cfmakeraw(getBufferAddress(termios_p.buffer));
        nativeToJava(termios_p);
    }
    
    public static int tcsetattr(int fd, int optional_actions, termios termios_p){
        javaToNative(termios_p);
        return tcsetattr(fd, optional_actions, getBufferAddress(termios_p.buffer));
    }
    
    @SuppressWarnings("Duplicates")
    public static int compress2(ByteBuffer dest, ByteBuffer source, int level){
        if(!Objects.requireNonNull(dest).isDirect() ||
            !Objects.requireNonNull(source).isDirect()){
            throw new IllegalArgumentException("Buffers must be direct!");
        }
        
        long destAddress = getBufferAddress(dest);
        long sourceAddress = getBufferAddress(source);
        
        ByteBuffer sizeBuffer = ByteBuffer.allocateDirect(Long.BYTES);
        sizeBuffer.order(ByteOrder.nativeOrder());
        sizeBuffer.putLong(0, source.limit());
        long sizeAddress = getBufferAddress(sizeBuffer);
        
        int result = compress2(destAddress, sizeAddress, sourceAddress, source.limit(), level);
        
        dest.position((int)sizeBuffer.getLong(0));
        
        return result;
    }
    
    private static void nativeToJava(termios termios_p){
        ByteBuffer buffer = termios_p.buffer;
        termios_p.c_iflag = buffer.getInt(0);
        termios_p.c_oflag = buffer.getInt(4);
        termios_p.c_cflag = buffer.getInt(8);
        termios_p.c_lflag = buffer.getInt(12);
        termios_p.c_line = (char)buffer.get(16);
        for(int i = 0; i < 19; i++){
            termios_p.c_cc[i] = (char)buffer.get(17 + i);
        }
    }
    
    private static void javaToNative(termios termios_p){
        ByteBuffer buffer = termios_p.buffer;
        buffer.putInt(0, termios_p.c_iflag);
        buffer.putInt(4, termios_p.c_oflag);
        buffer.putInt(8, termios_p.c_cflag);
        buffer.putInt(12, termios_p.c_lflag);
        buffer.put(16, (byte)termios_p.c_line);
        for(int i = 0; i < 19; i++){
            buffer.put(17 + i, (byte)termios_p.c_cc[i]);
        }
    }
    
    native private static int doGetRealFD(FileDescriptor descriptor);
    native private static long getBufferAddress(ByteBuffer buffer);
    native private static int getTermiosSize();
    
    native public static int fcntl(int fd, int cmd, int arg);
    native private static int tcgetattr(int fd, long termios_p);
    native private static void cfmakeraw(long termios_p);
    native private static int tcsetattr(int fd, int optional_actions, long termios_p);
    native public static int tcflush(int fd, int queue_selector);
    
    native private static int compress2(long dest, long destLength, long source, long sourceLen, int level);
    
    public static class termios{
    
        private ByteBuffer buffer;
        
        int c_iflag;
        int c_oflag;
        public int c_cflag;
        int c_lflag;
        char c_line;
        final char[] c_cc = new char[/*NCCS*/19];
    }
}
