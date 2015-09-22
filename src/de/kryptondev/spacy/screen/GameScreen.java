package de.kryptondev.spacy.screen;

import de.kryptondev.spacy.SpacyClient;
import de.kryptondev.spacy.data.Ship;
import de.kryptondev.spacy.input.KeyInputManager;
import de.kryptondev.spacy.input.MouseInputManager;
import de.kryptondev.spacy.share.Move;
import de.kryptondev.spacy.share.PlayerRotate;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.input.MouseButton;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.geom.Vector2f;




public class GameScreen implements IScreen, KeyInputManager.KeyListener, MouseInputManager.MouseListener {
    public static final org.newdawn.slick.Color BackgroundColor = new org.newdawn.slick.Color(8,8,64);
    private SpacyClient spacyClient;
    private Image background;
    private Vector2f lastMousePos;
    
    public GameScreen(IScreen prevScreen, SpacyClient spacyClient) {
       //TODO Disable prevScreen
        this.spacyClient = spacyClient;
        this.lastMousePos = new Vector2f(0, 0);
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
           

            Graphics g = new Graphics(width, height);
            g.clear();
           
            int max = width * height / 1000;
            System.out.println("Creating " + max + " stars for background...");
            for(int i = 0; i < max; i++){
                int rad = (int)(r.nextFloat() * 5 + 2);                
                g.fillOval(r.nextInt(width), r.nextInt(height), rad,rad);
            }
            
            g.flush();
            
            background = new Image(width, height);
            g.copyArea(background,0,0);
            
        } catch (SlickException ex) {
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
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
            g.fillRect(ship.bounds.x + ship.position.getX(), ship.bounds.y + ship.position.getY(), ship.bounds.width, ship.bounds.height);
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
    public void onKeyUp(int key) {
        
    }
    
    @Override
    public void onKeyPressed(int key) {
       
    }

    @Override
    public void onButtonDown(int button) {
        if(button == 1){
            Vector2f pos = MouseInputManager.getInstance().getPosition();
            if(lastMousePos != pos){
                lastMousePos = pos;
                //TODO Carls Vector shizzl :D
                SpacyClient.getInstance().getClient().sendTCP(new PlayerRotate(SpacyClient.instance.getShip().direction));
            }
        }
    }
    
    @Override
    public void onButtonUp(int button) {
        if(button == 1){
            SpacyClient.getInstance().getClient().sendTCP(new Move(true));
            System.out.println("Stop moving");
        }
    }

    @Override
    public void onButtonPressed(int button) {
        if(button == 1){
            SpacyClient.getInstance().getClient().sendTCP(new Move(true));
            System.out.println("Start moving");
        }
        
        
    }
    
}
