package de.kryptondev.spacy;

import de.kryptondev.spacy.input.KeyInputManager;
import de.kryptondev.spacy.input.MouseInputManager;
import de.kryptondev.spacy.screen.MainMenuScreen;
import de.kryptondev.spacy.screen.ScreenManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;

public class MainGame extends BasicGame {
    
    private KeyInputManager keyInputManager;
    private MouseInputManager mouseInputManager;
    private static ScreenManager screenManager;
    
    public MainGame() {
        super("Spacy");
    }
    
    @Override
    public void init(GameContainer gc) throws SlickException {
        keyInputManager = KeyInputManager.getInstance();
        mouseInputManager = new MouseInputManager();
        screenManager = ScreenManager.getInstance();
        
        screenManager.changeScreen(new MainMenuScreen());
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {        
        keyInputManager.update(gc.getInput());
        mouseInputManager.update();
        
        screenManager.update(gc, i);
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        screenManager.draw(gc, g);
    }
    
    public static void main(String[] args) {
        try {
            // Add LWJGL natives
            JavaHelper.addLibraryPath("natives/" + JavaHelper.getOperatingSystem());
            
            AppGameContainer gc = new AppGameContainer(new MainGame());
            gc.setDisplayMode(800, 600, false);
            gc.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static ScreenManager getScreenManager() {
        return screenManager;
    }

    public static void setScreenManager(ScreenManager screenManager) {
        MainGame.screenManager = screenManager;
    }
    
    
    
}
