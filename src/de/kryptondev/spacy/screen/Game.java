package de.kryptondev.spacy.screen;

import de.kryptondev.spacy.SpacyClient;
import de.kryptondev.spacy.data.Ship;
import de.kryptondev.spacy.input.KeyInputManager;
import de.kryptondev.spacy.input.MouseInputManager;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;




public class Game implements IScreen, KeyInputManager.KeyListener, MouseInputManager.MouseListener {
    public static final org.newdawn.slick.Color BackgroundColor = new org.newdawn.slick.Color(8,8,64);
    private SpacyClient spacyClient;
    private Image background;
   
    public Game(IScreen prevScreen, SpacyClient spacyClient) {
       //TODO Disable prevScreen
        this.spacyClient = spacyClient;
        
    }
    
    @Override
    public void init(GameContainer gc) {    
        MouseInputManager.getInstance().registerListener("Fire", Input.MOUSE_LEFT_BUTTON, this);       
        MouseInputManager.getInstance().registerListener("Throttle", Input.MOUSE_RIGHT_BUTTON, this);
        KeyInputManager.getInstance().registerListener("Throttle", Input.KEY_SPACE, this);
        Random r = new Random();
        
        try {
            
            int width = gc.getWidth();
            int height = gc.getHeight();            
           
            /*
            int width = 4096;
            int height = width;
            */
            Graphics g = new Graphics(width, height);
            g.clear();
            //g.setAntiAlias(true);
            int max = width * height / 1000;
            System.out.println("Creating " + max + " stars for background...");
            for(int i = 0; i < max; i++){
                int rad = (int)(r.nextFloat() * 5 + 2);                
                g.fillOval(r.nextInt(width), r.nextInt(height), rad,rad);
            }
            
            g.flush();
            
            background = new Image(width, height);
            g.copyArea(background,0,0);
            //TOOD: Fix
            
        } catch (SlickException ex) {
            Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
        }
        
      
            
    }

    @Override
    public void update(GameContainer gc, int delta) {
        
    }

    @Override
    public void draw(GameContainer gc, Graphics g) {
        g.setBackground(BackgroundColor);
        g.drawImage(background, 0, 0);
       
        
        if(spacyClient.getWorld() == null)
            return;
        for(Ship ship : spacyClient.getWorld().ships){
            g.fillRect(ship.bounds.getX() + ship.position.getX(), ship.bounds.getY() + ship.position.getY(), ship.bounds.getWidth(), ship.bounds.getHeight());
        }
        
        
        
        
    }
    
    public void onDisconnect() {
        
    }
    
    public void onRecv(Object o){
        
    }
    
    public void onConnected(){
        
    }
    
    @Override
    public void onKeyDown(int key) {
      
    }

    @Override
    public void onKeyPressed(int key) {
        
    }

    @Override
    public void onButtonDown(int button) {
        System.out.println(button);
    }

    @Override
    public void onButtonPressed(int button) {
        System.out.println(button);
    }


    
}
