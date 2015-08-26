package de.kryptondev.spacy;

import de.kryptondev.spacy.helper.Game;
import de.kryptondev.spacy.render.RenderState;
import de.kryptondev.spacy.render.Texture;
import org.lwjgl.opengl.Display;
import org.lwjgl.util.vector.Vector2f;

public class MainGame extends Game {

    private Texture logo;
    
    @Override
    public void init() {
        logo = new Texture();
    }

    @Override
    public void draw() {
        for(int i = 0; i < 100000; i++) {
            logo.draw(new Vector2f(i, i));
        }
    }

    @Override
    public void update(long delta) {
        if(delta != 0)
            Display.setTitle("FPS: " + Math.round((1000 / delta)));
        System.out.println(delta);
    }

    public static void main(String[] args) {
        MainGame game = new MainGame();
        game.run();
    }
}
