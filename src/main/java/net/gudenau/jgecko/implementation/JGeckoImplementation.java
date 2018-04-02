package net.gudenau.jgecko.implementation;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;
import net.gudenau.jgecko.GeckoDevice;
import net.gudenau.jgecko.GeckoIdentifier;

/**
 * The interface to define a USB Gecko platform.
 * */
public interface JGeckoImplementation{
    List<GeckoIdentifier> listSerialDevices();
    GeckoDevice open(GeckoIdentifier device) throws IOException;
    // Because the Java code does not work for HBC. .-.
    int compress2(ByteBuffer dest, ByteBuffer source, int level);
}
