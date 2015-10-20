package de.kryptondev.spacy.screen;

import de.kryptondev.spacy.data.SaveState;
import de.kryptondev.spacy.input.MouseInputManager;
import java.awt.Font;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.gui.TextField;

public class OptionScreen implements IScreen, MouseInputManager.MouseListener {

    private TrueTypeFont font;
    private TrueTypeFont fontBig;
    
    private TextField userName;
    private int width;
    
    private Rectangle saveRect;
    private Rectangle cancelRect;
    
    private int selected = 0;
    
    @Override
    public void init(GameContainer gc) {
        Font awtFont = new Font("Arial", Font.BOLD, 16);
        font = new TrueTypeFont(awtFont, true);
        
        awtFont = new Font("Arial", Font.BOLD, 20);
        fontBig = new TrueTypeFont(awtFont, true);
        
        width = 255;
        width += font.getWidth("Username: ");
        userName = new TextField(gc, font, gc.getWidth() / 2 - width / 2 + font.getWidth("Username: ") + 5, 125, 250, 25);
        userName.setBackgroundColor(Color.gray);
        userName.setBorderColor(null);
        
        saveRect = new Rectangle(
                gc.getWidth() / 2 - font.getWidth("Save") / 2, 
                gc.getHeight() - 130, 
                font.getWidth("Save"), 
                font.getHeight()
        );
        
        cancelRect = new Rectangle(
                gc.getWidth() / 2 - font.getWidth("Cancel") / 2, 
                gc.getHeight() - 100, 
                font.getWidth("Cancel"), 
                font.getHeight()
        );
        
        MouseInputManager.getInstance().registerListener("Left Button", Input.MOUSE_LEFT_BUTTON, this);
    }

    @Override
    public void update(GameContainer gc, int delta) {
        Vector2f pos = MouseInputManager.getInstance().getPosition();
        if(saveRect.contains(pos.x, pos.y)) {
            selected = 1;
        } else if(cancelRect.contains(pos.x, pos.y)) {
            selected = 2;
        } else {
            selected = 0;
        }
    }

    @Override
    public void draw(GameContainer gc, Graphics g) {
        font.drawString(gc.getWidth() / 2 - width / 2, 128, "Username: ");
        userName.render(gc, g);
        
        
        fontBig.drawString(gc.getWidth() / 2 - font.getWidth("Save") / 2, 
                gc.getHeight() - 130, "Save", selected == 1 ? Color.yellow : Color.white);
        fontBig.drawString(gc.getWidth() / 2 - font.getWidth("Cancel") / 2, 
                gc.getHeight() - 100, "Cancel", selected == 2 ? Color.yellow : Color.white);
    }

    @Override
    public void onButtonDown(int button) {
        
    }

    @Override
    public void onButtonUp(int button) {
        
    }

    @Override
    public void onButtonPressed(int button) {
        switch(selected) {
            case 1:
                save();
            case 2:
                close();
                break;
        }
    }
    
    public void save() {
        SaveState.getInstance().setPlayerName(userName.getText());
        SaveState.getInstance().save();
    }
    
    public void close() {
        ScreenManager.getInstance().changeScreen(new MainMenuScreen());
    }
    
}
