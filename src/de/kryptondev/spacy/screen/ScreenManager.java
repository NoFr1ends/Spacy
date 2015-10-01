package de.kryptondev.spacy.screen;

import de.kryptondev.spacy.input.KeyInputManager;
import de.kryptondev.spacy.input.MouseInputManager;
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
        System.out.println("Change screen to " + screen + " in the next frame...");
        newScreen = screen;
    }
    
    public IScreen getCurrentScreen(){
        return currentScreen;
    }
    
    public void draw(GameContainer gc, Graphics g) {
        if(currentScreen != null) {
            currentScreen.draw(gc, g);            
            g.resetTransform();
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
            KeyInputManager.getInstance().clear();
            MouseInputManager.getInstance().clear();
            
            currentScreen = newScreen;
            newScreen = null;
            currentScreen.init(gc);
            
            System.out.println("Screen changed to " + currentScreen);
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
