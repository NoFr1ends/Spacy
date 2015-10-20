package de.kryptondev.spacy.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;

public class SaveState implements Serializable {

    private static SaveState instance;
    private String playerName;
    
    public SaveState() {
        playerName = "Steve";
    }
    
    public static SaveState getInstance() {
        if(instance == null)
            instance = SaveState.load();
        
        return instance;
    }
    
    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }
    
    public void save() {
        try {
            ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream("options.dat"));
            out.writeObject(this);
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static SaveState load() {
        File file = new File("options.dat");
        if(file.exists()) {
            try {
                ObjectInputStream in = new ObjectInputStream(new FileInputStream("options.dat"));
                SaveState ret = (SaveState)in.readObject();
                in.close();
                
                return ret;
            } catch (Exception e) {
                e.printStackTrace();
                
                return new SaveState();
            }
        } else {
            return new SaveState();
        }
    }
    
}
