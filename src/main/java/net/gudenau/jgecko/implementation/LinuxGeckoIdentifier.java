package net.gudenau.jgecko.implementation;

import net.gudenau.jgecko.GeckoIdentifier;

class LinuxGeckoIdentifier implements GeckoIdentifier{
    private final String device;
    
    LinuxGeckoIdentifier(String device){
        this.device = device;
    }
    
    String getDevice(){
        return device;
    }
    
    @Override
    public String toString(){
        return getDevice();
    }
    
    @Override
    public String getLabel(){
        return getDevice();
    }
}
