package net.gudenau.jgecko.natives;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import net.gudenau.jgecko.JGecko;

class Loader{
    static void loadNatives(){
        String os = System.getProperty("os.name").toLowerCase();
        String arch = System.getProperty("os.arch").toLowerCase();
        String extension;
        
        if(os.equals("linux")){
            extension = ".so";
        }else{
            throw new RuntimeException("Unsupported OS: " + os + " " + arch);
        }
    
        InputStream resourceStream = JGecko.class.getResourceAsStream("natives/" + os + "/" + arch + "/JGecko" + extension);
        if(resourceStream == null){
            throw new RuntimeException("Unsupported OS: " + os + " " + arch);
        }
        
        try{
            File tmpNative = File.createTempFile("jgecko", extension);
            try(OutputStream outputStream = new FileOutputStream(tmpNative);
                resourceStream){
                resourceStream.transferTo(outputStream);
            }
            System.load(tmpNative.getAbsolutePath());
        }catch(Exception e){
            throw new RuntimeException("Failed to make tmp native file!", e);
        }
    }
}
