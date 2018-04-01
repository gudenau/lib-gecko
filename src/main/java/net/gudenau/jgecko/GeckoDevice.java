package net.gudenau.jgecko;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * The interface for an opened USB Gecko (or clone).
 * */
public interface GeckoDevice extends Closeable{
    /**
     * Returns the {@link java.io.InputStream InputStream}
     * used to read data from the USB Gecko.
     *
     * @return The Gecko InputStream
     * */
    InputStream getInputStream();
    /**
     * Returns the {@link java.io.OutputStream OutputStream}
     * used to read data from the USB Gecko.
     *
     * @return The Gecko InputStream
     * */
    OutputStream getOutputStream();
}
