package net.gudenau.jgecko.test;

import net.gudenau.jgecko.GeckoDevice;
import net.gudenau.jgecko.GeckoIdentifier;
import net.gudenau.jgecko.JGecko;
import net.gudenau.jgecko.helper.HBC;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.util.List;

public class Test{
    public static void main(String[] arguments) throws Throwable{
        List<GeckoIdentifier> devices = JGecko.listSerialDevices();
    
        GeckoIdentifier device;
        if(devices.size() > 1){
            System.out.println("Select device:");
            for(int i = 0, devicesSize = devices.size(); i < devicesSize; i++){
                GeckoIdentifier dev = devices.get(i);
                System.out.println("\t" + i + "> " + dev);
            }
    
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))){
                device = devices.get(Integer.parseInt(reader.readLine()) - 1);
            }
        }else{
            device = devices.get(0);
        }
        
        try(GeckoDevice geckoDevice = JGecko.open(device)){
            HBC.wiiLoad(geckoDevice, new File("src/test/wii/test.dol"));
        }
    }
}
