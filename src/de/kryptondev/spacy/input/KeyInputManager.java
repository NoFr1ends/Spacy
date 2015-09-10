package de.kryptondev.spacy.input;

import java.util.HashMap;
import java.util.Map;
import org.lwjgl.input.Keyboard;

public class KeyInputManager {
    
    private static KeyInputManager instance;
    private HashMap<Integer, HashMap<String, KeyListener>> listeners;
    
    public KeyInputManager() {
        listeners = new HashMap<>();
    }
    
    public static KeyInputManager getInstance() {
        return instance;
    }
    
    public void registerListener(String name, int key, KeyListener listener) {
        if(!listeners.containsKey(key)) {
            listeners.put(key, new HashMap<>());
        }
        
        listeners.get(key).put(name, listener);
    }
    
    public void update() {
        for(Map.Entry<Integer, HashMap<String, KeyListener>> keyEvents: 
                listeners.entrySet()) {
            for(Map.Entry<String, KeyListener> events: 
                    keyEvents.getValue().entrySet()) {
                // Todo implement on pressed
                
                if(Keyboard.isKeyDown(keyEvents.getKey())) {
                    events.getValue().onKeyDown(keyEvents.getKey());
                }
            }
        }
    }
    
    public static interface KeyListener {
        public void onKeyDown(int key);
        public void onKeyPressed(int key);
    }
    
}
