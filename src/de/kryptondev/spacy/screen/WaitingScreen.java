package de.kryptondev.spacy.screen;

import java.awt.Font;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.TrueTypeFont;


public class WaitingScreen implements IScreen {
    private int y = 100;
    private int x = 0;
    private TrueTypeFont font;
    private volatile String statusMessage = "";
    public WaitingScreen() {
    }

    public WaitingScreen(String initialMessage) {
        statusMessage = initialMessage;
    }

    
    @Override
    public void init(GameContainer gc) {
        Font awtFont = new Font("Arial", Font.BOLD, 20);
        font = new TrueTypeFont(awtFont, true);
        
    }

    @Override
    public void update(GameContainer gc, int delta) {
        x = (gc.getWidth() / 2 - font.getWidth(getStatusMessage()) / 2);
    }

    @Override
    public void draw(GameContainer gc, Graphics g) {
        g.setColor(Color.white);
        g.drawString(getStatusMessage(), x, y);
    }

    public String getStatusMessage() {
        return statusMessage;
    }

    public void setStatusMessage(String statusMessage) {
        this.statusMessage = statusMessage;
    }
    
}
