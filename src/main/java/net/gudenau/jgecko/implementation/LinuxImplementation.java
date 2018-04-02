package net.gudenau.jgecko.implementation;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

import net.gudenau.jgecko.GeckoDevice;
import net.gudenau.jgecko.GeckoIdentifier;
import net.gudenau.jgecko.natives.Linux;

/**
 * The Linux platform specific code paths.
 * */
public class LinuxImplementation implements JGeckoImplementation{
    @Override
    public List<GeckoIdentifier> listSerialDevices(){
        File ttyDir = new File("/sys/class/tty/");
        File[] ttyFiles = ttyDir.listFiles();
        if(ttyFiles == null){
            return Collections.emptyList();
        }
        List<String> devices = new LinkedList<>();
        for(File device : ttyFiles){
            if(new File(device, "device/driver").exists()){
                File tmpFile = new File("/dev/" + device.getName());
                if(tmpFile.canRead() && tmpFile.canWrite()){
                    devices.add(device.getName());
                }
            }
        }
        devices.sort(String::compareTo);
        return Collections.unmodifiableList(devices.stream().map(LinuxGeckoIdentifier::new).collect(Collectors.toList()));
    }
    
    @Override
    public GeckoDevice open(GeckoIdentifier device) throws IOException{
        return new LinuxGeckoDevice(device);
    }
    
    @Override
    public int compress2(ByteBuffer dest, ByteBuffer source, int level){
        return Linux.compress2(dest, source, level);
    }
}
