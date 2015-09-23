package de.kryptondev.spacy.screen;

import de.kryptondev.spacy.data.*;
import de.kryptondev.spacy.share.Move;
import de.kryptondev.spacy.share.PlayerRotate;
import de.kryptondev.spacy.SpacyClient;
import de.kryptondev.spacy.SpriteSheet;
import de.kryptondev.spacy.input.KeyInputManager;
import de.kryptondev.spacy.input.MouseInputManager;


import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
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
    private Vector2f viewPort;
    private SpriteSheet spriteSheet;

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
                g.fillOval(r.nextInt(width), r.nextInt(height), rad, rad);
            }
            
            g.flush();
            
            background = new Image(width, height);
            g.copyArea(background,0,0);
            System.out.println("Done.");
            
        } catch (SlickException ex) {
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        spriteSheet = new SpriteSheet("data/sheet.xml");        
      
    }

    @Override
    public void update(GameContainer gc, int delta) {
        /*Vector2f shipPos = this.spacyClient.getShip().position;
        Rect shipBound = this.spacyClient.getShip().bounds;
        */
        
        
        //TODO: Positioning for "ViewPort"
        
        //TODO: Edit local "smooth" movements
    }

    @Override
    public void draw(GameContainer gc, Graphics g) {
        g.setBackground(BackgroundColor);
        g.drawImage(background, 0, 0);
        
        //spriteSheet.draw("cockpitBlue_0.png" , 0, 0);
        
        if(spacyClient.getWorld() == null)
            return;
        CopyOnWriteArrayList<Ship> ships = new CopyOnWriteArrayList<>( spacyClient.getWorld().ships);
        for(Ship ship : ships){            
            Vector2f renderPosition =ship.getRenderPos();
            g.fillRect(renderPosition.x, renderPosition.y, ship.bounds.width, ship.bounds.height);
        }
        CopyOnWriteArrayList<Projectile> pros = new CopyOnWriteArrayList<>( spacyClient.getWorld().projectiles);
        for(Projectile p : pros){
            Vector2f renderPosition = p.getRenderPos();
            g.fillRect(renderPosition.x, renderPosition.y, p.bounds.width, p.bounds.height);
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
            SpacyClient.getInstance().getShip().rotateToMouse(pos);
            SpacyClient.getInstance().getClient().sendTCP(new PlayerRotate(SpacyClient.instance.getShip().direction));

        }
        
    }
    
    @Override
    public void onButtonUp(int button) {
        //Move
        if(button == 1){
            SpacyClient.getInstance().getClient().sendTCP(new Move(false));
            System.out.println("Stop moving");
        }
    }

    @Override
    public void onButtonPressed(int button) {
        //Rotate
        Vector2f pos = MouseInputManager.getInstance().getPosition();
        SpacyClient.getInstance().getShip().rotateToMouse(pos);
        SpacyClient.getInstance().getClient().sendTCP(new PlayerRotate(SpacyClient.instance.getShip().direction));
        //Move
        if(button == 1){
            SpacyClient.getInstance().getClient().sendTCP(new Move(true));
            System.out.println("Start moving");
        }
        //Fire
        if(button == 0){
            //TODO Implement Weapon Cooldown

            Ship ship = SpacyClient.instance.getShip();          
            SpacyClient.getInstance().getClient().sendTCP(
                    new Projectile(DamageType.balistic, ship.id, ship.direction, ship.position));
        }
        
    }
    
}
