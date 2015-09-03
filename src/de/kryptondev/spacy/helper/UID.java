package de.kryptondev.spacy.helper;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.security.MessageDigest;

/**
 * Generates a UID for the current computer 
 */
public class UID {
    public static byte[] getUID() {
       
        StringBuilder s = new StringBuilder();
        s.append(System.getProperty("os.name"));
        s.append(System.getProperty("os.arch"));        
        s.append(System.getProperty("user.home"));
        s.append(System.getProperty("java.home"));
        s.append(System.getProperty("file.separator"));
        s.append(System.getenv("NUMBER_OF_PROCESSORS"));
        s.append(System.getenv("PROCESSOR_IDENTIFIER"));
        try{            
            
            
            InetAddress ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);

            byte[] mac = network.getHardwareAddress();
            
            for (int i = 0; i < mac.length; i++) {
                s.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));		
            }
        
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            md.update(s.toString().getBytes("UTF-8"));
            return md.digest();            
        }
        catch(Exception ex) {
            return new byte[] {};
        }
        
        
        
       
    }
}
