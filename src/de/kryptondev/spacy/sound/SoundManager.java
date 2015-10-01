package de.kryptondev.spacy.sound;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import org.lwjgl.openal.AL;
import org.newdawn.slick.openal.Audio;
import org.newdawn.slick.openal.AudioLoader;
import org.newdawn.slick.util.ResourceLoader;

public class SoundManager {    
    private static SoundManager instance;
    private HashMap<String, Audio> sounds;
    private HashMap<String, Audio> music;

    private void load(String path, HashMap<String, Audio> targetList)throws IOException{
        File folder = new File(path);
        if(folder.exists() && folder.isDirectory() && folder.canRead()){
            File[] sounds = folder.listFiles();
            
            for(File file : sounds){
                if (file.isFile()) {
                    targetList.put(file.getName(), AudioLoader.getAudio("OGG", ResourceLoader.getResourceAsStream(file.getAbsolutePath())));
                }
            }
            
        }
        else{
            if(!folder.exists() || !folder.isDirectory()){
                throw new IOException("Invalid path! " + path);
            }
            else{
                throw new IOException("No permissions on " + path);
            }
        }
    }
    
    public void loadSounds(String path) throws IOException{
        load(path, sounds);
    }
    
    public void loadMusic(String path)throws IOException{
        load(path, music);
    }
    
    public static SoundManager getInstance(){
        if(instance == null){
            instance = new SoundManager();
        }
        return instance;
    }
    public void dispose(){
        AL.destroy();
    }
}