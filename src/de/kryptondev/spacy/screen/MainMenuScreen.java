package de.kryptondev.spacy.screen;

import de.kryptondev.spacy.MainGame;
import de.kryptondev.spacy.input.KeyInputManager;
import java.awt.Font;
import java.util.ArrayList;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;

public class MainMenuScreen implements IScreen, KeyInputManager.KeyListener {

    private ArrayList<String> menuEntries;
    private TrueTypeFont font;
    private int currentEntry;
    
    private static final String START_GAME = "Start game";
    private static final String OPTIONS = "Options";
    private static final String EXIT_GAME = "Exit game";
    
    private boolean exit;
    
    public MainMenuScreen() {
        menuEntries = new ArrayList<>();
        menuEntries.add(START_GAME);
        menuEntries.add(OPTIONS);
        menuEntries.add(EXIT_GAME);
    }
    
    @Override
    public void init(GameContainer gc) {
        Font awtFont = new Font("Arial", Font.BOLD, 20);
        font = new TrueTypeFont(awtFont, true);
        
        KeyInputManager.getInstance().registerListener(
                "Menu Down", 
                Input.KEY_DOWN, 
                this
        );
        
        KeyInputManager.getInstance().registerListener(
                "Menu Up", 
                Input.KEY_UP, 
                this
        );
        
        KeyInputManager.getInstance().registerListener(
                "Menu Enter", 
                Input.KEY_ENTER, 
                this
        );
    }

    @Override
    public void update(GameContainer gc, int delta) {
        if(exit) 
            gc.exit();
    }

    @Override
    public void draw(GameContainer gc, Graphics g) {
        int y = 100;
        int i = 0;
        for(String entry: menuEntries) {
            Color color = Color.white;
            if(currentEntry == i) {
                color = Color.yellow;
            }
            
            font.drawString((gc.getWidth() - font.getWidth(entry)) / 2, 
                    y, entry, color);
            
            y+=font.getHeight(entry) + 10;
            
            i++;
        }
    }
    
    public void onMenuPressed(String entry) {
        switch(entry) {
            case EXIT_GAME:
                exit = true;
                break;
            case START_GAME:
                MainGame.getScreenManager().changeScreen(new ConnectMenuScreen(this));
                break;
        }
    }

    @Override
    public void onKeyDown(int key) {
        
    }

    @Override
    public void onKeyPressed(int key) {
        if(key == Input.KEY_DOWN) {
            currentEntry++;
            if(currentEntry == menuEntries.size()) {
                currentEntry = 0;
            }
        } else if(key == Input.KEY_UP) {
            currentEntry--;
            if(currentEntry < 0) {
                currentEntry = menuEntries.size() - 1;
            }
        } else if(key == Input.KEY_ENTER) {
            onMenuPressed(menuEntries.get(currentEntry));
        }
    }
    
}
