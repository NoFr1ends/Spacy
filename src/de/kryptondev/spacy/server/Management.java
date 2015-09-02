package de.kryptondev.spacy.server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Management {
    public static ArrayList<byte[]> getAdmins() {
         try {
             
            ArrayList<byte[]> admins = new ArrayList<>(16);
            FileReader fr = new FileReader("admins.txt");
            BufferedReader br = new BufferedReader(fr);

            String line = "";

            while ((line = br.readLine()) != null) {  
                if(line.length() == 32)
                    admins.add(line.getBytes("UTF-8"));
            }

            br.close();
            
            return admins;
        } catch (Exception ex) {
            return new ArrayList<>(0);
        } 
    }
    
    public static ArrayList<byte[]> getLocalBannedClients() {
        try {
            ArrayList<byte[]> bans = new ArrayList<>(16);
            FileReader fr = new FileReader("banned.txt");
            BufferedReader br = new BufferedReader(fr);

            String line = "";

            while ((line = br.readLine()) != null) {  
                if(line.length() == 32)
                    bans.add(line.getBytes("UTF-8"));
            }

            br.close();
            
            return bans;
        } catch (Exception ex) {
            return new ArrayList<>(0);
        }
    }

    //TODO Implement
    //Low-Prio
    public static boolean hasGlobalBan(byte[] uid) {
        return false;
    }
}
