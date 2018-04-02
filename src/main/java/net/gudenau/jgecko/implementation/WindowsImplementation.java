package net.gudenau.jgecko.implementation;

import net.gudenau.jgecko.GeckoDevice;
import net.gudenau.jgecko.GeckoIdentifier;
import net.gudenau.jgecko.natives.Windows;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * The Linux platform specific code paths.
 * */
public class WindowsImplementation implements JGeckoImplementation{
    @Override
    public List<GeckoIdentifier> listSerialDevices(){
        String[][] flatDeviceMap = Windows.listSerialDevices();
        
        if(flatDeviceMap == null){
            return Collections.emptyList();
        }
        
        List<GeckoIdentifier> devices = new LinkedList<>();
        for(String[] entry : flatDeviceMap){
            devices.add(new WindowsGeckoIdentifier(entry[0], entry[1]));
        }
        return devices;
    }
    
    @Override
    public GeckoDevice open(GeckoIdentifier device) throws IOException{
        return new WindowsGeckoDevice(device);
    }
    
    @Override
    public int compress2(ByteBuffer dest, ByteBuffer source, int level){
        return Windows.compress2(dest, source, level);
    }
}
