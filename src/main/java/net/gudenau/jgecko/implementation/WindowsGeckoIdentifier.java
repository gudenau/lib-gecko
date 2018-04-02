package net.gudenau.jgecko.implementation;

import net.gudenau.jgecko.GeckoIdentifier;

class WindowsGeckoIdentifier implements GeckoIdentifier{
    private final String name;
    private final String value;
    
    WindowsGeckoIdentifier(String name, String value){
        this.name = name;
        this.value = value;
    }
    
    String getName(){
        return name;
    }
    
    String getValue(){
        return value;
    }
    
    @Override
    public String toString(){
        return getValue();
    }
    
    @Override
    public String getLabel(){
        return getValue();
    }
}
