package net.gudenau.jgecko.implementation;

import java.io.IOException;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.util.Objects;

/**
 * A simple wrapper to allow stream access to a
 * {@link java.io.RandomAccessFile RandomAccessFile}.
 * */
class RandomAccessFileInputStream extends InputStream{
    private final RandomAccessFile file;
    
    RandomAccessFileInputStream(RandomAccessFile file){
        this.file = Objects.requireNonNull(file);
    }
    
    @Override
    public int read() throws IOException{
        return file.read();
    }
    
    @Override
    public int read(byte b[]) throws IOException {
        return file.read(b);
    }
    
    @Override
    public int read(byte b[], int off, int len) throws IOException {
        return file.read(b, off, len);
    }
    
    @Override
    public long skip(long n) throws IOException {
        if(n <= 0){
            return 0;
        }
        
        long pointer = file.getFilePointer();
        long size = file.length();
        long distance = n;
        
        if(distance + pointer > size){
            distance = size - pointer;
        }
        file.seek(pointer + distance);
        
        return distance;
    }
    
    @Override
    public int available() throws IOException {
        long available = file.length() - file.getFilePointer();
        return available > Integer.MAX_VALUE ? Integer.MAX_VALUE : (int)available;
    }
    
    @Override
    public void close() throws IOException {
        file.close();
    }
}
