package de.kryptondev.spacy.screen;

import de.kryptondev.spacy.input.KeyInputManager;
import de.kryptondev.spacy.input.MouseInputManager;
import java.awt.Font;
import java.util.LinkedHashMap;
import java.util.Map;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;

public class MainMenuScreen implements IScreen, KeyInputManager.KeyListener {

    private LinkedHashMap<String, Object> menuEntries;
    private TrueTypeFont font;
    private int currentEntry;
    
    public MainMenuScreen() {
        menuEntries = new LinkedHashMap<>();
        menuEntries.put("Start game", null);
        menuEntries.put("Exit game", null);
    }
    
    @Override
    public void init(GameContainer gc) {
        Font awtFont = new Font("Arial", Font.BOLD, 20);
        font = new TrueTypeFont(awtFont, true);
        
        KeyInputManager.getInstance().registerListener(
                "Menu Down", 
                Input.KEY_DOWN, 
                this
        );
    }

    @Override
    public void update(GameContainer gc, int delta) {
        
    }

    @Override
    public void draw(GameContainer gc, Graphics g) {
        int y = 100;
        int i = 0;
        for(Map.Entry<String, Object> entry: menuEntries.entrySet()) {
            Color color = Color.white;
            if(currentEntry == i) {
                color = Color.yellow;
            }
            
            font.drawString((gc.getWidth() - font.getWidth(entry.getKey())) / 2, 
                    y, entry.getKey(), color);
            
            y+=font.getHeight(entry.getKey()) + 10;
            
            i++;
        }
    }

    @Override
    public void onKeyDown(int key) {
        
    }

    @Override
    public void onKeyPressed(int key) {
        currentEntry++;
        if(currentEntry == menuEntries.size()) {
            currentEntry = 0;
        }
    }
    
}
