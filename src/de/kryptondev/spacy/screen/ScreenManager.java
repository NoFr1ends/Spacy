package de.kryptondev.spacy.screen;

import java.awt.Font;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;

public class ScreenManager {

    private static ScreenManager instance;
    
    private IScreen currentScreen;
    private IScreen newScreen;
    
    private TrueTypeFont font;
    
    public ScreenManager() {
        Font awtFont = new Font("Arial", Font.BOLD, 12);
        font = new TrueTypeFont(awtFont, true);
    }
    
    public void changeScreen(IScreen screen) {
        newScreen = screen;
    }
    
    public void draw(GameContainer gc, Graphics g) {
        if(currentScreen != null) {
            currentScreen.draw(gc, g);
            
            String className = currentScreen.getClass().getSimpleName();
            font.drawString(
                    gc.getWidth() - font.getWidth(className), 
                    gc.getHeight() - font.getHeight(className), 
                    className
            );
        }
    }
    
    public void update(GameContainer gc, int delta) {
        if(newScreen != null) {
            currentScreen = newScreen;
            currentScreen.init(gc);
            newScreen = null;
        }
        
        if(currentScreen != null) {
            currentScreen.update(gc, delta);
        }
    }
    
    public static ScreenManager getInstance() {
        if(instance == null)
            instance = new ScreenManager();
        
        return instance;
    }
    
}
