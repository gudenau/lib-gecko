package net.gudenau.jgecko.implementation;

import net.gudenau.jgecko.GeckoDevice;
import net.gudenau.jgecko.GeckoIdentifier;
import static net.gudenau.jgecko.natives.Windows.*;

import java.io.*;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Objects;

public class WindowsGeckoDevice implements GeckoDevice{
    private final long fd;
    
    private final InputStream inputStream;
    private final OutputStream outputStream;
    
    WindowsGeckoDevice(GeckoIdentifier device) throws IOException{
        fd = CreateFile(((WindowsGeckoIdentifier)device).getValue(), GENERIC_READ | GENERIC_WRITE, 0, 0, OPEN_EXISTING, FILE_ATTRIBUTE_NORMAL, 0);
    
        COMMTIMEOUTS timeouts = new COMMTIMEOUTS();
        
        if(!GetCommTimeouts(fd, timeouts)){
            throw new IOException(String.format("error reading timeouts from port 0x%08X", GetLastError()));
        }
        
        timeouts.ReadIntervalTimeout = MAXDWORD;
        timeouts.ReadTotalTimeoutMultiplier = 0;
        timeouts.ReadTotalTimeoutConstant = 0;
        timeouts.WriteTotalTimeoutMultiplier = 0;
        timeouts.WriteTotalTimeoutConstant = 0;
        
        if(!SetCommTimeouts(fd, timeouts)){
            throw new IOException(String.format("error setting timeouts on port 0x%08X", GetLastError()));
        }
    
        if (!SetCommMask(fd, 0)) {
            throw new IOException(String.format("error setting communications event mask 0x%08X", GetLastError()));
        }
        
        flush();
        
        inputStream = new WindowsInputStream();
        outputStream = new WindowsOutputStream();
    }
    
    private void flush() throws IOException{
        if(!PurgeComm(fd, PURGE_RXCLEAR | PURGE_TXCLEAR | PURGE_TXABORT | PURGE_RXABORT)){
            throw new IOException(String.format("Failed to flush: 0x%08X", GetLastError()));
        }
    }
    
    @Override
    public InputStream getInputStream(){
        return inputStream;
    }
    
    @Override
    public OutputStream getOutputStream(){
        return outputStream;
    }
    
    @Override
    public void close() throws IOException{
        if(!CloseHandle(fd)){
            throw new IOException(String.format("Failed to close handle 0x%08X", GetLastError()));
        }
    }
    
    public class COMMTIMEOUTS{
        public final ByteBuffer buffer;
        
        int ReadIntervalTimeout;
        int ReadTotalTimeoutMultiplier;
        int ReadTotalTimeoutConstant;
        int WriteTotalTimeoutMultiplier;
        int WriteTotalTimeoutConstant;
        
        COMMTIMEOUTS(){
            buffer = ByteBuffer.allocateDirect(getCOMMTIMEOUTSSize());
            buffer.order(ByteOrder.nativeOrder());
        }
    
        public void readFromNative(){
            ReadIntervalTimeout = buffer.getInt(0);
            ReadTotalTimeoutMultiplier = buffer.getInt(4);
            ReadTotalTimeoutConstant = buffer.getInt(8);
            WriteTotalTimeoutMultiplier = buffer.getInt(12);
            WriteTotalTimeoutConstant = buffer.getInt(16);
        }
        
        public void writeToNative(){
            buffer.putInt(0, ReadIntervalTimeout);
            buffer.putInt(4, ReadTotalTimeoutMultiplier);
            buffer.putInt(8, ReadTotalTimeoutConstant);
            buffer.putInt(12, WriteTotalTimeoutMultiplier);
            buffer.putInt(16, WriteTotalTimeoutConstant);
        }
    }
    
    private class WindowsInputStream extends InputStream{
        @Override
        public int read() throws IOException{
            byte[] buffer = new byte[1];
            if(read(buffer) != 1){
                return -1;
            }
            return buffer[0];
        }
    
        @Override
        public int read(byte[] b) throws IOException {
            Objects.requireNonNull(b);
            return read(b, 0, b.length);
        }
    
        @Override
        public int read(byte[] b, int off, int len) throws IOException {
            Objects.requireNonNull(b);
            Objects.checkFromIndexSize(off, len, b.length);
            
            ByteBuffer buffer = ByteBuffer.allocateDirect(len);
            int length = ReadFile(fd, buffer, len);
            buffer.get(b, off, len);
            return length;
        }
        
        @Override
        public int available() throws IOException{
            return 0;
        }
    }
    
    private class WindowsOutputStream extends OutputStream{
        @Override
        public void write(int b) throws IOException{
            write(new byte[]{(byte)b});
        }
    
        @Override
        public void write(byte[] b) throws IOException {
            Objects.requireNonNull(b);
            write(b, 0, b.length);
        }
    
        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            Objects.requireNonNull(b);
            Objects.checkFromIndexSize(off, len, b.length);
        
            ByteBuffer buffer = ByteBuffer.allocateDirect(len);
            buffer.put(b, off, len);
            buffer.position(0);
            WriteFile(fd, buffer, len);
        }
        
        @Override
        public void flush() throws IOException {
            WindowsGeckoDevice.this.flush();
        }
    }
}
