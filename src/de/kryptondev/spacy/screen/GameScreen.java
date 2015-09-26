package de.kryptondev.spacy.screen;

import de.kryptondev.spacy.data.*;
import de.kryptondev.spacy.share.Move;
import de.kryptondev.spacy.share.PlayerRotate;
import de.kryptondev.spacy.SpacyClient;
import de.kryptondev.spacy.SpriteSheet;
import de.kryptondev.spacy.input.KeyInputManager;
import de.kryptondev.spacy.input.MouseInputManager;
import java.util.ArrayList;


import java.util.Random;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.newdawn.slick.Color;
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
    private Rect viewPort;
    private SpriteSheet spriteSheet;
    private Ship myShip;
    private final int backgroundMoveFactor = 2;
    private Vector2f viewPortCenter;
    private float zoom = 1.0f;
    private final float zoomStep = 0.2f;
    private final Random rand;
    private final int maxBackgroundLayers = 3;
    private boolean debug = false;
    
    //Der letzte Zeitpunkt, andem das PlayerRotate-Paket gesendet wurde.
    private long timeLastPlayerRotate = 0;
    //Zeit (Ticks) die gewartet wird, bis das nächste PlayerRotate-Paket gesendet wird. 
    private long sendFreq = 100;
    private SpriteSheet sheet;
    private boolean moving;
    
    public GameScreen(IScreen prevScreen, SpacyClient spacyClient) {
       //TODO Disable prevScreen
        this.spacyClient = spacyClient;
        this.lastMousePos = new Vector2f(0, 0);
        rand = new Random();      
    }
    
    public float getRandomFloat(float min, float max){
        return rand.nextFloat() * (max - min) + min;
    }
    
    @Override
    public void init(GameContainer gc) {    
        MouseInputManager.getInstance().registerListener("Fire", Input.MOUSE_LEFT_BUTTON, this);       
        MouseInputManager.getInstance().registerListener("Throttle", Input.MOUSE_RIGHT_BUTTON, this);
        KeyInputManager.getInstance().registerListener("Debug", Input.KEY_F12, this);

        Random r = new Random();
        gc.setAlwaysRender(true);
        
        // Load main sprite sheet
        sheet = new SpriteSheet("data/sheet.xml");
        
        try {
            
            int width = gc.getWidth();
            int height = gc.getHeight();      
           
            viewPort = new Rect(0, 0, width, height);

            

            Graphics g = new Graphics(width, height);
            g.clear();

            int max = width * height / 800;
            
            for(int i = 0; i < max; i++){
                int rad = (int)(getRandomFloat(1.2f,5));                
                g.fillOval(rand.nextInt(width), rand.nextInt(height), rad, rad);
            }

            g.flush();

            background = new Image(width, height);
            g.copyArea(background,0,0);                
            g.destroy();
            
        } catch (SlickException ex) {
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
        spriteSheet = new SpriteSheet("data/sheet.xml");        
      
    }

    @Override
    public void update(GameContainer gc, int delta) {
        for(Entity e: (ArrayList<Entity>)spacyClient.getWorld().getAllEntities()) {
            e.move(delta);
        }
       
        
        if(this.spacyClient.getShip() == null){
            //Last death point?
            //viewPortCenter = lastDeath;
        }
        else
        {
            Vector2f shipPos = this.spacyClient.getShip().position;
            this.viewPortCenter = shipPos;
            this.viewPort.x = viewPortCenter.x - (this.viewPort.width / 2);
            this.viewPort.y = viewPortCenter.y - (this.viewPort.height / 2);
        }
    }
    
    @Override
    public void draw(GameContainer gc, Graphics g) {        
        g.translate((-viewPort.x) / this.backgroundMoveFactor , 
                (-viewPort.y) / this.backgroundMoveFactor);
        g.scale(zoom, zoom);
        g.setColor(Color.white);        
        g.setBackground(BackgroundColor);
        g.drawImage(background, 0, 0);
        g.resetTransform();
        g.scale(zoom, zoom);
        g.translate((-viewPort.x * zoom), (-viewPort.y * zoom));
        //spriteSheet.draw("cockpitBlue_0.png" , 0, 0);
        if(spacyClient.getWorld() == null)
            return;
        CopyOnWriteArrayList<Ship> ships = new CopyOnWriteArrayList<>( spacyClient.getWorld().ships);
        for(Ship ship : ships){            
            Vector2f renderPosition = ship.getCenteredRenderPos();
            
            sheet.draw(ship.texture, renderPosition.x, renderPosition.y);
        }
        g.setColor(Color.yellow);
        CopyOnWriteArrayList<Projectile> pros = new CopyOnWriteArrayList<>( spacyClient.getWorld().projectiles);
        for(Projectile p : pros){
            Vector2f renderPosition = p.getBulletRenderPos();
            sheet.draw(p.texture, renderPosition.x, renderPosition.y);
            //Vector2f bounds = p.getBounds();
            //g.fillRect(renderPosition.x, renderPosition.y, bounds.x, bounds.y);
        }        
        
        
        
        /*
        DEBUG SECTION
        */
        if(this.debug){
            g.resetTransform();
            g.setColor(Color.white);
            g.drawString("Serverdelta:     " + spacyClient.getServerTickDelta() + "ms", 8, 30);
            g.drawString("Ticks/s:         " + spacyClient.getServerTicksPerSecond(), 8, 50);            
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
       if(key == Input.KEY_ADD){
           this.zoom *= 1 + zoomStep;
           this.viewPort.height *= 1 + zoomStep;
           this.viewPort.width *= 1 + zoomStep;
           System.out.println(viewPort.toString());
           
       }
       if(key == Input.KEY_SUBTRACT){
           this.zoom *= 1 - zoomStep;
           this.viewPort.height *= 1 - zoomStep;
           this.viewPort.width *= 1 - zoomStep;
           System.out.println(viewPort.toString());
       }
       //Toggle debug
       if(key == Input.KEY_F12){
           debug = !debug;
       }
    }
    private void print(String id, Vector2f vec){
        System.out.println(id+  "@ " + vec.x + ", " + vec.y);
    }
    @Override
    public void onButtonDown(int button) {
        if(button == 1){
            if(timeLastPlayerRotate + sendFreq >= System.currentTimeMillis()){
                Vector2f pos = MouseInputManager.getInstance().getPosition();
                SpacyClient.getInstance().getShip().direction = new Vector2f(pos).sub(new Vector2f(viewPort.width / 2, viewPort.height / 2)).normalise();            
                SpacyClient.getInstance().getClient().sendTCP(new PlayerRotate(SpacyClient.instance.getShip().direction));
            }
        }
        
    }
    
    @Override
    public void onButtonUp(int button) {
        //Move
        if(button == 1){
            SpacyClient.getInstance().getClient().sendTCP(new Move(false));
            System.out.println("Stop moving");
            moving = false;
        }
    }

    @Override
    public void onButtonPressed(int button) {
        //Rotate
        Vector2f pos = MouseInputManager.getInstance().getPosition();

        SpacyClient.getInstance().getShip().direction = new Vector2f(pos).sub(new Vector2f(viewPort.width / 2, viewPort.height / 2)).normalise();
        SpacyClient.getInstance().getClient().sendTCP(new PlayerRotate(SpacyClient.instance.getShip().direction));

        //Move
        if(button == 1){
            SpacyClient.getInstance().getClient().sendTCP(new Move(true));
            System.out.println("Start moving");
            moving = true;
        }
        //Fire
        if(button == 0){
            //TODO Implement Weapon Cooldown
           
            SpacyClient.getInstance().getClient().sendTCP(
                    new Projectile(DamageType.balistic, myShip.id, myShip.direction, myShip.position));
        }
        
    }


    public Rect getViewPort() {
        return viewPort;
    }

    public void setViewPort(Rect viewPort) {
        this.viewPort = viewPort;
    }


    public Ship getMyShip() {
        return myShip;
    }

    public void setMyShip(Ship myShip) {
        this.myShip = myShip;
    }
    
    
}
