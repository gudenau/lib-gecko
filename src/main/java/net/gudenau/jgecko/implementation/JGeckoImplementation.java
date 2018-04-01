package net.gudenau.jgecko.implementation;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import net.gudenau.jgecko.GeckoDevice;

/**
 * The interface to define a USB Gecko platform.
 * */
public interface JGeckoImplementation{
    List<String> listSerialDevices();
    GeckoDevice open(String device) throws IOException;
    // Because the Java code does not work for HBC. .-.
    int compress2(ByteBuffer dest, ByteBuffer source, int level);
}
