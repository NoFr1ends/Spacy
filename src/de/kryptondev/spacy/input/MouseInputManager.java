package de.kryptondev.spacy.input;

import java.util.HashMap;
import java.util.Map;

import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Vector2f;

//TODO FIX Warum wird listeners zurückgesetzt?!
public class MouseInputManager {
    
    private static MouseInputManager instance;
    
    private HashMap<Integer, HashMap<String, MouseListener>> listeners;
    private HashMap<Integer, Boolean> lastStates;
    
    private Vector2f position = new Vector2f();
    
    private boolean firstUpdate = true;
    
    public MouseInputManager() {
        listeners = new HashMap<>();
        lastStates = new HashMap<>();
    }
    
    public static MouseInputManager getInstance() {
        if(instance == null)
            instance = new MouseInputManager();
        return instance;
    }
    
    public Vector2f getPosition() {
        return position;
    }
    
    public void registerListener(String name, int button, MouseListener listener) {
        if(!listeners.containsKey(button)) {
            listeners.put(button, new HashMap<String, MouseListener>());
        }
        
        listeners.get(button).put(name, listener);
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
    
    public void update(Input input) {
        position = new Vector2f(input.getMouseX(), input.getMouseY());
        
        if(listeners == null || listeners.isEmpty()){
            return;
        }
        for(Map.Entry<Integer, HashMap<String, MouseListener>> mouseEvents: 
                listeners.entrySet()) {
            for(Map.Entry<String, MouseListener> events: 
                    mouseEvents.getValue().entrySet()) {
                if(input.isMouseButtonDown(mouseEvents.getKey())) {
                    events.getValue().onButtonDown(mouseEvents.getKey());
                    
                    if(!getLastState(mouseEvents.getKey())) {
                        events.getValue().onButtonPressed(mouseEvents.getKey());
                    }
                } else if (getLastState(mouseEvents.getKey()) && !firstUpdate) {
                    events.getValue().onButtonUp(mouseEvents.getKey());
                }       
            }
            
            setLastState(mouseEvents.getKey(), input.isMouseButtonDown(mouseEvents.getKey()));
        }
        
        firstUpdate = false;
    }

    public void clear() {
        listeners.clear();
        firstUpdate = true;
    }
   
    
    
    public static interface MouseListener {
        public void onButtonDown(int button);
        public void onButtonUp(int button);
        public void onButtonPressed(int button);
    }
    
}
