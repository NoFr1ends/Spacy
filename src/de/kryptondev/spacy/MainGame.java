package de.kryptondev.spacy;

import de.kryptondev.spacy.input.KeyInputManager;
import de.kryptondev.spacy.input.MouseInputManager;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.SlickException;

public class MainGame extends BasicGame {
    
    private KeyInputManager keyInputManager;
    private MouseInputManager mouseInputManager;
    
    public MainGame() {
        super("Spacy");
    }
    
    @Override
    public void init(GameContainer gc) throws SlickException {
        keyInputManager = new KeyInputManager();
        mouseInputManager = new MouseInputManager();
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        keyInputManager.update();
        mouseInputManager.update();
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        
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
    
}
