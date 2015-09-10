package de.kryptondev.spacy.input;

import java.util.HashMap;
import org.lwjgl.util.vector.Vector2f;

public class MouseInputManager {
    
    private static MouseInputManager instance;
    
    private HashMap<Integer, HashMap<String, MouseListener>> listeners;
    
    public MouseInputManager() {
        
    }
    
    public static MouseInputManager getInstance() {
        return instance;
    }
    
    public Vector2f getPosition() {
        return new Vector2f(0, 0);
    }
    
    public void registerListener(String name, int button, MouseListener listener) {
        
    }
    
    public void update() {
        
    }
    
    public static interface MouseListener {
        public void onButtonDown(int button);
        public void onButtonPressed(int button);
    }
    
}
