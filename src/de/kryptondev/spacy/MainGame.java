package de.kryptondev.spacy;

import de.kryptondev.spacy.data.BitmapFont;
import de.kryptondev.spacy.data.SpriteSheet;
import de.kryptondev.spacy.helper.Game;
import de.kryptondev.spacy.render.ColoredRect;
import de.kryptondev.spacy.render.RenderState;
import de.kryptondev.spacy.render.Texture;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;
import org.lwjgl.util.vector.Vector2f;
import org.lwjgl.util.vector.Vector4f;

public class MainGame extends Game {

    private Texture logo;
    private Texture fullSheet;
    private ColoredRect test;
    
    private BitmapFont font;
    
    @Override
    public void init() {
        SpriteSheet sheet = new SpriteSheet("data/sheet.xml");

        logo = sheet.getTexture("playerLife1_blue.png");
        test = new ColoredRect(100, 100, new Vector4f(1, 0, 0, 1));
        
        font = new BitmapFont("data/kenpixel_16.fnt");
        
        fullSheet = new Texture("data/sheet.png");
    }

    @Override
    public void draw() {
        /*for(int i = 0; i < 100000; i++) {
            logo.draw(new Vector2f(i, i));
        }*/
        
        logo.draw(new Vector2f(0, 0));
        
        test.draw(new Vector2f(100, 100));
        
        font.getCharacter('A').draw(50, 50);
        
        //logo.draw(new Vector2f(100, 100));
        
        //fullSheet.draw(new Vector2f(50, 50));
        
        System.out.println("-----------------------------");
    }

    @Override
    public void update(long delta) {
        if(Keyboard.isKeyDown(Keyboard.KEY_ESCAPE)) 
            close();
        
        if(delta != 0)
            Display.setTitle("FPS: " + Math.round((1000 / delta)));
        //System.out.println(delta);
    }

    public static void main(String[] args) {
        MainGame game = new MainGame();
        game.run();
    }
}
