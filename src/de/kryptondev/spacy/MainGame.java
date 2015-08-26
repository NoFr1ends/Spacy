package de.kryptondev.spacy;

import de.kryptondev.spacy.helper.Game;
import de.kryptondev.spacy.render.RenderState;
import de.kryptondev.spacy.render.Texture;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

public class MainGame extends Game {

    private Texture logo;
    private Texture fullSheet;
    
    @Override
    public void init() {
        SpriteSheet sheet = new SpriteSheet("data/sheet.xml");

        logo = sheet.getTexture("buttonBlue.png");
        
        fullSheet = new Texture("data/sheet.png");
    }

    @Override
    public void draw() {
        /*for(int i = 0; i < 100000; i++) {
            logo.draw(new Vector2f(i, i));
        }*/
        
        logo.draw(new Vector2f(0, 0));
        logo.draw(new Vector2f(100, 100));
        
        fullSheet.draw(new Vector2f(50, 50));
    }

    @Override
    public void update(long delta) {
        if(delta != 0)
            Display.setTitle("FPS: " + Math.round((1000 / delta)));
        //System.out.println(delta);
    }

    public static void main(String[] args) {
        MainGame game = new MainGame();
        game.run();
    }
}
