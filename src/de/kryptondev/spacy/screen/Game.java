package de.kryptondev.spacy.screen;

import de.kryptondev.spacy.SpacyClient;
import de.kryptondev.spacy.data.Ship;
import de.kryptondev.spacy.input.KeyInputManager;
import de.kryptondev.spacy.input.MouseInputManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import javafx.scene.paint.Color;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;



public class Game implements IScreen, KeyInputManager.KeyListener, MouseInputManager.MouseListener {
    private final org.newdawn.slick.Color backgroundColor = new org.newdawn.slick.Color(8,8,64);
    private ArrayList<test> backgroundStars;
    private SpacyClient spacyClient;
    
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
        backgroundStars = new ArrayList(1000);
    }
    
    @Override
    public void init(GameContainer gc) {    
        MouseInputManager.getInstance().registerListener("Fire", Input.MOUSE_LEFT_BUTTON, this);       
        MouseInputManager.getInstance().registerListener("Throttle", Input.MOUSE_RIGHT_BUTTON, this);
        KeyInputManager.getInstance().registerListener("Throttle", Input.KEY_SPACE, this);
        Random r = new Random();
        for(int i = 0; i < 10000; i++){
           backgroundStars.add(new test(r.nextInt(2048), r.nextInt(2048), r.nextFloat() * 5));
        }
    }

    @Override
    public void update(GameContainer gc, int delta) {
        
    }

    @Override
    public void draw(GameContainer gc, Graphics g) {
        g.setBackground(backgroundColor);
        if(spacyClient.getWorld() == null)
            return;
        for(Ship ship : spacyClient.getWorld().ships){
            g.fillRect(ship.bounds.getX() + ship.position.getX(), ship.bounds.getY() + ship.position.getY(), ship.bounds.getWidth(), ship.bounds.getHeight());
        }
        
        
        /*for(test star : backgroundStars)
            g.fillOval(star.pos.x, star.pos.y, star.size, star.size);
        */
        
        
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
