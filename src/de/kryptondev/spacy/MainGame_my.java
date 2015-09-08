package de.kryptondev.spacy;

import java.io.FileInputStream;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;

public class MainGame extends BasicGame {

    private Texture texture;
    
    public MainGame() {
        super("Spacy");
    }
    
    @Override
    public void init(GameContainer gc) throws SlickException {
        //gc.setShowFPS(false);
        
        try {
            texture = TextureLoader.getTexture("PNG", new FileInputStream("data/sheet.png"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void update(GameContainer gc, int i) throws SlickException {
        
    }

    @Override
    public void render(GameContainer gc, Graphics g) throws SlickException {
        Image i = new Image(texture);
        
        //g.drawImage(i, 0, 0);
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
