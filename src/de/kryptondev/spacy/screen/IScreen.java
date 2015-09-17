package de.kryptondev.spacy.screen;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;

public interface IScreen {
    
    public void init(GameContainer gc);
    public void update(GameContainer gc, int delta);
    public void draw(GameContainer gc, Graphics g);
    
}
