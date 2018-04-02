package net.gudenau.jgecko.implementation;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import net.gudenau.jgecko.GeckoDevice;
import net.gudenau.jgecko.GeckoIdentifier;

import static net.gudenau.jgecko.natives.Linux.*;

/**
 * Linux Gecko device implementation.
 * */
public class LinuxGeckoDevice implements GeckoDevice{
    private final RandomAccessFile file;
    private final int fileDescriptor;
    
    private final InputStream inputStream;
    private final OutputStream outputStream;
    
    LinuxGeckoDevice(GeckoIdentifier device) throws IOException{
        file = new RandomAccessFile("/dev/" + ((LinuxGeckoIdentifier)device).getDevice(), "rw");
        fileDescriptor = getRealFD(file.getFD());
        
        if(fcntl(fileDescriptor, F_SETFL, 0) != 0){
            throw new IOException("F_SETFL on serial port");
        }
    
        termios newtio = new termios();
        if(tcgetattr(fileDescriptor, newtio) != 0){
            throw new IOException("tcgetattr");
        }
    
        cfmakeraw(newtio);
    
        newtio.c_cflag |= CRTSCTS | CS8 | CLOCAL | CREAD;
    
        if(tcsetattr(fileDescriptor, TCSANOW, newtio) != 0){
            throw new IOException("tcsetattr");
        }
    
        flush();
        
        inputStream = new RandomAccessFileInputStream(file);
        outputStream = new GeckoOutputStream(file);
    }
    
    private void flush() throws IOException{
        if(tcflush(fileDescriptor, TCIOFLUSH) != 0){
            throw new IOException("tcflush");
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
        file.close();
    }
    
    private class GeckoOutputStream extends RandomAccessFileOutputStream{
        GeckoOutputStream(RandomAccessFile file){
            super(file);
        }
    
        @Override
        public void flush() throws IOException {
            LinuxGeckoDevice.this.flush();
        }
    }
}
