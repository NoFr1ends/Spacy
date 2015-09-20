package de.kryptondev.spacy.screen;

import de.kryptondev.spacy.SpacyClient;
import de.kryptondev.spacy.data.Ship;
import de.kryptondev.spacy.input.KeyInputManager;
import de.kryptondev.spacy.input.MouseInputManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.paint.Color;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.opengl.pbuffer.FBOGraphics;



public class Game implements IScreen, KeyInputManager.KeyListener, MouseInputManager.MouseListener {
    private final org.newdawn.slick.Color backgroundColor = new org.newdawn.slick.Color(8,8,64);
    private SpacyClient spacyClient;
    private Image background;
    public class test{
        public Vector2f pos;
        public float size;

        public test(int x, int y, float size) {
            this.pos = new Vector2f(x,y);
            this.size = size;
        }
        
    }
    
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
           
            Graphics g = new Graphics(width,height);
            int max = gc.getWidth() * gc.getHeight() / 1000;
            for(int i = 0; i < max; i++){
                int rad = (int)(r.nextFloat() * 6);                
                g.fillOval(r.nextInt(width), r.nextInt(height), rad,rad);
            }
            
            background = new Image(width, height);
            g.copyArea(background,100,100);
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
        g.setBackground(backgroundColor);
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
