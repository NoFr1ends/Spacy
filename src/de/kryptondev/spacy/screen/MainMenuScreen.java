package de.kryptondev.spacy.screen;

import de.kryptondev.spacy.MainGame;
import de.kryptondev.spacy.SpriteSheet;
import de.kryptondev.spacy.input.KeyInputManager;
import de.kryptondev.spacy.input.MouseInputManager;
import static de.kryptondev.spacy.screen.GameScreen.BackgroundColor;
import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Vector2f;

public class MainMenuScreen implements IScreen, KeyInputManager.KeyListener, 
        MouseInputManager.MouseListener {

    private ArrayList<String> menuEntries;
    private HashMap<String, Rectangle> rectCache;
    
    private TrueTypeFont font;    
    private TrueTypeFont titleFont;

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
        
        // TODO: Try to implement load from ttf file
        awtFont = new Font("Geometos", Font.BOLD, 60);
        titleFont = new TrueTypeFont(awtFont, true);
        
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
        
        MouseInputManager.getInstance().registerListener(
                "Left Button", 
                Input.MOUSE_LEFT_BUTTON, 
                this
        );
    }

    @Override
    public void update(GameContainer gc, int delta) {
        fillCache(gc);
        
        String menuHover = getMenuPointAtMouse();
        if(menuHover != null) {
            currentEntry = menuEntries.indexOf(menuHover);
        }
        
        if(exit) 
            gc.exit();
    }

    @Override
    public void draw(GameContainer gc, Graphics g) {
        g.setBackground(BackgroundColor);
        titleFont.drawString((gc.getWidth() - titleFont.getWidth("SPACY")) / 2, 
                20, "SPACY", Color.white);
        int y = 300;
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
        
        //sheet.draw("playerShip1_blue.png", 0, 0);
    }
    
    private void fillCache(GameContainer gc) {
        if(rectCache != null)
            return;
        
        rectCache = new HashMap<>();
        
        int y = 300;
        int i = 0;
        for(String entry: menuEntries) {
            int width = font.getWidth(entry);
            int height = font.getHeight();
            
            Rectangle bounds = new Rectangle(
                    (gc.getWidth() - width) / 2, 
                    y, 
                    width, 
                    height
            );
            y += height + 10;
            
            rectCache.put(entry, bounds);
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
    public void onKeyUp(int key) {
        
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

    @Override
    public void onButtonDown(int button) {
    }
    
    @Override
    public void onButtonUp(int button) {   
    }

    @Override
    public void onButtonPressed(int button) {
        String menuPoint = getMenuPointAtMouse();
        if(menuPoint != null)
            onMenuPressed(menuPoint);
    }
    
    private String getMenuPointAtMouse() {
        Vector2f mousePos = MouseInputManager.getInstance().getPosition();
        
        for(Map.Entry<String, Rectangle> cache: rectCache.entrySet()) {
            if(cache.getValue().contains((int)mousePos.x, (int)mousePos.y)) {                
                return cache.getKey();
            }
        }
        
        return null;
    }
    
}
