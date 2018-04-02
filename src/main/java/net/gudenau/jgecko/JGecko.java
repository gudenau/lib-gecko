package net.gudenau.jgecko;

import java.io.IOException;
import java.util.List;

import static net.gudenau.jgecko.implementation.ImplementationHolder.getImplementation;

/**
 * Used to discover and open USB Gecko (and clones).
 * */
public class JGecko{
    /**
     * Gets a list of serial devices available to be opened.
     *
     * @return A list of serial devices
     * */
    public static List<GeckoIdentifier> listSerialDevices(){
        return getImplementation().listSerialDevices();
    }
    
    /**
     * Opens a connection to a USB Gecko.
     *
     * @param device The name of the serial device
     *
     * @return The opened device
     * */
    public static GeckoDevice open(GeckoIdentifier device) throws IOException{
        return getImplementation().open(device);
    }
}
