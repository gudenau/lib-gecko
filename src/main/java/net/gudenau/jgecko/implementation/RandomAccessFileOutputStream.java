package net.gudenau.jgecko.implementation;

import java.io.IOException;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.util.Objects;

/**
 * A simple wrapper to allow stream access to a
 * {@link java.io.RandomAccessFile RandomAccessFile}.
 * */
class RandomAccessFileOutputStream extends OutputStream{
    private final RandomAccessFile file;
    
    RandomAccessFileOutputStream(RandomAccessFile file){
        this.file = Objects.requireNonNull(file);
    }
    
    @Override
    public void write(int b) throws IOException{
        file.write(b);
    }
    
    @Override
    public void write(byte b[]) throws IOException{
        file.write(b);
    }
    
    @Override
    public void write(byte b[], int off, int len) throws IOException{
        file.write(b, off, len);
    }
    
    @Override
    public void close() throws IOException{
        file.close();
    }
}
