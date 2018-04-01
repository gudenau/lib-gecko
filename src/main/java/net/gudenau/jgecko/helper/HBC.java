package net.gudenau.jgecko.helper;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import net.gudenau.jgecko.GeckoDevice;

import static net.gudenau.jgecko.implementation.ImplementationHolder.getImplementation;

/**
 * A small helper class that enables sending data to
 * the Wii's Homebrew Channel.
 * */
public class HBC{
    private static final int MAX_ARGS_LEN = 1024;
    
    /**
     * Attempts to send a file to the HBC.
     *
     * @param gecko The {@link net.gudenau.jgecko.GeckoDevice GeckoDevice} instance
     * @param file The {@link java.io.File File} to send
     * @param params The params for the file
     * */
    public static void wiiLoad(GeckoDevice gecko, File file, String ... params) throws IOException{
        try(InputStream inputStream = new FileInputStream(file)){
            wiiLoad(gecko, inputStream, params);
        }
    }
    
    /**
     * Attempts to send a data to the HBC from an
     * {@link java.io.InputStream InputStream}.
     *
     * @param gecko The {@link net.gudenau.jgecko.GeckoDevice GeckoDevice} instance
     * @param data The {@link java.io.InputStream InputStream} to read from
     * @param params The params for the file
     * */
    public static void wiiLoad(GeckoDevice gecko, InputStream data, String ... params) throws IOException{
        ByteArrayOutputStream paramOutputStream = new ByteArrayOutputStream(MAX_ARGS_LEN);
        for(String param : params){
            paramOutputStream.write(param.getBytes(StandardCharsets.UTF_8));
            paramOutputStream.write(0);
        }
        if(paramOutputStream.size() > MAX_ARGS_LEN){
            throw new IllegalArgumentException("Too many params!");
        }
        byte[] paramData = paramOutputStream.toByteArray();
        
        OutputStream outputStream = gecko.getOutputStream();
        outputStream.write(new byte[]{
            'H', 'A', 'X', 'X',
            0, 5,
            (byte)((paramData.length >> 8) & 0xFF),
            (byte)(paramData.length & 0xFF)
        });
        
        byte[] originalPayload = data.readAllBytes();
        ByteBuffer originalBuffer = ByteBuffer.allocateDirect(originalPayload.length);
        originalBuffer.put(originalPayload).position(0);
        
        ByteBuffer compressedBuffer = ByteBuffer.allocateDirect((int)Math.ceil(originalPayload.length * 1.02));
        getImplementation().compress2(compressedBuffer, originalBuffer, 6);
    
        byte[] compressedPayload = new byte[compressedBuffer.position()];
        compressedBuffer.position(0).get(compressedPayload);
        
        int len = compressedPayload.length;
        outputStream.write(new byte[]{
            (byte)((len >> 24) & 0xff),
            (byte)((len >> 16) & 0xff),
            (byte)((len >> 8) & 0xff),
            (byte)(len & 0xff)
        });
        len = originalPayload.length;
        outputStream.write(new byte[]{
            (byte)((len >> 24) & 0xff),
            (byte)((len >> 16) & 0xff),
            (byte)((len >> 8) & 0xff),
            (byte)(len & 0xff)
        });
        
        outputStream.write(compressedPayload);
    }
}
