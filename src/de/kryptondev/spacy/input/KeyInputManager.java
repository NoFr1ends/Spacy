package de.kryptondev.spacy.input;

import java.util.HashMap;
import java.util.Map;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Input;

public class KeyInputManager {
    
    private static KeyInputManager instance;
    private HashMap<Integer, HashMap<String, KeyListener>> listeners;
    private HashMap<Integer, Boolean> lastStates;
    
    public KeyInputManager() {
        listeners = new HashMap<>();
        lastStates = new HashMap<>();
    }
    
    public static KeyInputManager getInstance() {
        if(instance == null) {
            instance = new KeyInputManager();
        }
        
        return instance;
    }
    
    //TODO Remove Listener
    
    public void registerListener(String name, int key, KeyListener listener) {
        if(!listeners.containsKey(key)) {
            listeners.put(key, new HashMap<String, KeyListener>());
        }
        
        listeners.get(key).put(name, listener);
    }
    
    public void update(Input input) {
        for(Map.Entry<Integer, HashMap<String, KeyListener>> keyEvents: 
                listeners.entrySet()) {
            for(Map.Entry<String, KeyListener> events: 
                    keyEvents.getValue().entrySet()) {
                // Todo implement on pressed
                
                //System.out.println("Check key " + keyEvents.getKey());
                if(input.isKeyDown(keyEvents.getKey())) {
                    events.getValue().onKeyDown(keyEvents.getKey());
                    
                    if(!getLastState(keyEvents.getKey())) {
                        events.getValue().onKeyPressed(keyEvents.getKey());
                    }
                }
            }
            
            setLastState(keyEvents.getKey(), input.isKeyDown(keyEvents.getKey()));
        }
    }
    
    private boolean getLastState(int key) {
        if(!lastStates.containsKey(key)) {
            lastStates.put(key, true);
        }
        
        return lastStates.get(key);
    }
    
    private void setLastState(int key, boolean state) {
        lastStates.put(key, state);
    }
    
    public static interface KeyListener {
        public void onKeyDown(int key);
        public void onKeyPressed(int key);
    }
    
}
