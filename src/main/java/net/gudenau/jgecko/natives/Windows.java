package net.gudenau.jgecko.natives;

import net.gudenau.jgecko.implementation.WindowsGeckoDevice;

import java.io.FileDescriptor;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

public class Windows{
    static{
        Loader.loadNatives();
    }
    
    public static final int MAXDWORD = 0xFFFFFFFF;
    
    public static final int PURGE_TXABORT = 0x0001;
    public static final int PURGE_RXABORT = 0x0002;
    public static final int PURGE_TXCLEAR = 0x0004;
    public static final int PURGE_RXCLEAR = 0x0008;
    
    public static final int GENERIC_READ = 0x80000000;
    public static final int GENERIC_WRITE = 0x40000000;
    public static final int OPEN_EXISTING = 3;
    public static final int FILE_ATTRIBUTE_NORMAL = 0x00000080;
    
    public static long CreateFile(String filename, int desiredAccess, int shareMode, long securityAttributes, int creaionDisposition, int flangsAndAttributes, long templateFile){
        return doCreateFile(Objects.requireNonNull(filename), desiredAccess, shareMode, securityAttributes, creaionDisposition, flangsAndAttributes, templateFile);
    }
    
    public static boolean CloseHandle(long handle){
        if(handle == 0){
            throw new NullPointerException();
        }else{
            return doCloseHandle(handle);
        }
    }
    
    native public static String[][] listSerialDevices();
    
    public static boolean GetCommTimeouts(long fd, WindowsGeckoDevice.COMMTIMEOUTS timeouts){
        boolean result = GetCommTimeouts(fd, getBufferAddress(timeouts.buffer));
        timeouts.readFromNative();
        return result;
    }
    
    public static boolean SetCommTimeouts(long fd, WindowsGeckoDevice.COMMTIMEOUTS timeouts){
        timeouts.writeToNative();
        return SetCommTimeouts(fd, getBufferAddress(timeouts.buffer));
    }
    
    public static int ReadFile(long fd, ByteBuffer buffer, int len) throws IOException{
        int[] read = new int[1];
        if(!ReadFile(fd, getBufferAddress(buffer), len, read, 0)){
            throw new IOException(String.format("Failed to read: 0x%08X", GetLastError()));
        }
        return read[0];
    }
    
    public static void WriteFile(long fd, ByteBuffer buffer, int len) throws IOException{
        int[] write = new int[1];
        int offset = 0;
        while(offset < len){
            if(!WriteFile(fd, getBufferAddress(buffer) + offset, len - offset, write, 0)){
                throw new IOException(String.format("Failed to write: 0x%08X", GetLastError()));
            }
            offset += write[0];
        }
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
    
    native private static long doCreateFile(String filename, int desiredAccess, int shareMode, long securityAttributes, int creaionDisposition, int flangsAndAttributes, long templateFile);
    native private static boolean doCloseHandle(long handle);
    native private static long getBufferAddress(ByteBuffer buffer);
    native public static int getCOMMTIMEOUTSSize();
    native private static boolean GetCommTimeouts(long fd, long buffer);
    native private static boolean SetCommTimeouts(long fd, long buffer);
    native public static boolean SetCommMask(long fd, int mask);
    native public static boolean PurgeComm(long fd, int flags);
    native public static int GetLastError();
    native private static boolean ReadFile(long fd, long bufferAddress, int len, int[] read, long overlapped);
    native private static boolean WriteFile(long fd, long bufferAddress, int len, int[] write, long overlapped);
    native private static int compress2(long destAddress, long sizeAddress, long sourceAddress, int limit, int level);
}
