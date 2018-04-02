package net.gudenau.jgecko.implementation;

/**
 * Just a small thing to keep an instance of
 * the OS implementation specific object.
 * */
public class ImplementationHolder{
    private static JGeckoImplementation implementation;
    
    /**
     * Gets the platform specific implementation instance.
     *
     * @return The implementation instance
     * */
    public static JGeckoImplementation getImplementation(){
        if(implementation == null){
            String os = System.getProperty("os.name");
            if(os.equals("Linux")){
                implementation = new LinuxImplementation();
            }else if(os.toLowerCase().contains("windows")){
                implementation = new WindowsImplementation();
            }else{
                throw new RuntimeException("Unknown OS: " + os);
            }
        }
        return implementation;
    }
}
