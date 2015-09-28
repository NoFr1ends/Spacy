package de.kryptondev.spacy.screen;

import de.kryptondev.spacy.data.*;
import de.kryptondev.spacy.share.Move;
import de.kryptondev.spacy.share.PlayerRotate;
import de.kryptondev.spacy.SpacyClient;
import de.kryptondev.spacy.SpriteSheet;
import de.kryptondev.spacy.input.KeyInputManager;
import de.kryptondev.spacy.input.MouseInputManager;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
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
    private SpacyClient client;
    private Image background;
    private Rect viewPort;
    private final int backgroundMoveFactor = 2;
    private Vector2f viewPortCenter = new Vector2f(0f, 0f);
    private float zoom = 1.0f;
    private final float zoomStep = 0.5f;
    private final Random rand;
    private boolean debug = false;
    
    //Der letzte Zeitpunkt, andem das PlayerRotate-Paket gesendet wurde.
    //private long timeLastPlayerRotate = 0;
    //Zeit (Ticks) die gewartet wird, bis das nächste PlayerRotate-Paket gesendet wird. 
    //private final long sendFreq = 100;
    private SpriteSheet sheet;
    
    public GameScreen(IScreen prevScreen, SpacyClient spacyClient) {
        this.client = spacyClient;
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
        
        KeyInputManager.getInstance().registerListener("Zoom In", Input.KEY_ADD, this);        
        KeyInputManager.getInstance().registerListener("Zoom Out", Input.KEY_SUBTRACT, this);
         KeyInputManager.getInstance().registerListener("Zoom Reset", Input.KEY_NUMPAD0, this);

        Random r = new Random();
        gc.setAlwaysRender(true);
        
        // Load main sprite sheet
        sheet = new SpriteSheet("data/sheet.xml");
        
        try {       
            viewPort = new Rect(0, 0, gc.getWidth(), gc.getHeight());
            
            int width = this.client.getWorld().worldSize;
            int height = width;      

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
    }

    @Override
    public void update(GameContainer gc, int delta) {
        
        try{
            ConcurrentHashMap<Long, Ship> ships = new ConcurrentHashMap<>(client.getWorld().ships);
            for(Ship ship : ships.values()){
                ship.move(delta);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        try{
            ConcurrentHashMap<Long, Projectile> projectiles = new ConcurrentHashMap<>(client.getWorld().projectiles);
            for(Projectile p : projectiles.values()){
                p.move(delta);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        try{
            ConcurrentHashMap<Long, Entity> entities = new ConcurrentHashMap<>(client.getWorld().entities);
            for(Entity e : entities.values()){
                e.move(delta);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
 
        if(client.getShip() == null){
            //Last death point?
            //viewPortCenter = lastDeath;
            System.err.println("MyShip is NULL");
        }
        else
        {
            Vector2f shipPos = client.getShip().position;
            this.viewPortCenter = shipPos;
            this.viewPort.x = (viewPortCenter.x - (this.viewPort.width / 2));
            this.viewPort.y = (viewPortCenter.y - (this.viewPort.height / 2));
            
        }
    }
    
    public void drawCross(Vector2f pos, Graphics g){
      
        float size = 50f;
        g.setColor(Color.magenta);
        g.drawLine(pos.x - size / 2, pos.y, pos.x + size / 2, pos.y);
        g.drawLine(pos.x, pos.y - size/ 2, pos.x, pos.y +  size / 2);
        g.setColor(Color.white);
        g.drawString(pos.x + " | " + pos.y , pos.x + 10, pos.y - 10);        
    }
    
    public void drawCross(float x, float y, Graphics g){
        drawCross(new Vector2f(x,y),g);
    }
    
    @Override
    public void draw(GameContainer gc, Graphics g) {    
        //g.setWorldClip(0, 0, client.getWorld().worldSize, client.getWorld().worldSize);
        g.drawImage(background, 0, 0);
        g.scale(zoom, zoom);
        g.translate((-viewPort.x) / this.backgroundMoveFactor , 
                (-viewPort.y) / this.backgroundMoveFactor);
        g.setBackground(BackgroundColor);
        //g.drawImage(background, 0, 0);
        g.resetTransform();
        g.scale(zoom, zoom);
        g.translate((-viewPort.x) , (-viewPort.y));

        if(client.getWorld() == null)
            return;
        
        //sheet.draw("meteorBrown_big1.png", viewPortCenter.x - 101 / 2, viewPortCenter.y-84/2);
//        drawCross(viewPortCenter.x, viewPortCenter.y, g);
        try{
            ConcurrentHashMap<Long, Ship> ships = new ConcurrentHashMap<>(client.getWorld().ships);
            for(ConcurrentHashMap.Entry<Long, Ship> ship : ships.entrySet()){                 
                Vector2f renderPosition = ship.getValue().getCenteredRenderPos();            
                sheet.draw(ship.getValue().texture, renderPosition.x, renderPosition.y);
                if(this.debug){
                    drawCross(ship.getValue().position.x, ship.getValue().position.y, g);
                    ship.getValue().drawRotation(g);
                    ship.getValue().drawBounds(g);
                }
            }        
            ConcurrentHashMap<Long, Projectile> projectiles = new ConcurrentHashMap<>(client.getWorld().projectiles);
            for(ConcurrentHashMap.Entry<Long, Projectile> p : projectiles.entrySet()){
                
                Vector2f renderPosition = (p).getValue().getBulletRenderPos();
                sheet.draw(p.getValue().texture, renderPosition.x, renderPosition.y);  
                if(this.debug){
                    drawCross(p.getValue().position.x, p.getValue().position.y, g);
                    p.getValue().drawRotation(g);
                    p.getValue().drawBounds(g);
                }
            }        
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        
        
        /*
        DEBUG SECTION
        */
        if(this.debug){
            g.resetTransform();
            g.setColor(Color.white);
            g.drawString("Serverdelta:     " + client.getServerTickDelta() + "ms", 8, 30);
            g.drawString("Ticks/s:         " + client.getServerTicksPerSecond(), 8, 50);            
            g.drawString("Viewport Pos:    " + this.viewPort.x + " | " + this.viewPort.y, 8, 70);
            g.drawString("Ship Pos:        " + this.viewPortCenter.x + " | " + this.viewPortCenter.y, 8, 90);
            g.drawString("Zoom:            " + this.zoom, 8, 110);
            g.drawString("Viewport Size:   " + this.viewPort.width + "x" + this.viewPort.height, 8, 130);
        }
        //g.setClip((int)viewPort.x, (int)viewPort.y, (int)(viewPort.width * zoom), (int)(viewPort.height* zoom));
        
        
      
        
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
//           this.viewPort.height *= 1 - zoomStep;
//           this.viewPort.width *= 1 - zoomStep;
           System.out.println(viewPort.toString());
           
       }
       if(key == Input.KEY_SUBTRACT){
           this.zoom *= 1 - zoomStep;
//           this.viewPort.height *= 1 + zoomStep;
//           this.viewPort.width *= 1 + zoomStep;
           System.out.println(viewPort.toString());
       }
       if(key == Input.KEY_NUMPAD0){
           
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
            //if(timeLastPlayerRotate + sendFreq >= System.currentTimeMillis()){
                Vector2f pos = MouseInputManager.getInstance().getPosition();
                Ship ship = client.getShip();
                ship.direction = new Vector2f(pos).sub(new Vector2f(viewPort.width / 2, viewPort.height / 2)).normalise();
                client.setShip(ship);
                SpacyClient.getInstance().getClient().sendTCP(new PlayerRotate(client.getShip().direction));
            //    timeLastPlayerRotate = System.currentTimeMillis();
            //}
        }
        
    }
    
    @Override
    public void onButtonUp(int button) {
        //Move
        if(button == 1){
            SpacyClient.getInstance().getClient().sendTCP(new Move(EMoving.Deccelerating, client.getShip().id));
            System.out.println("Stop moving");
        }
    }

    @Override
    public void onButtonPressed(int button) {
        //Rotate
        Ship myShip = client.getShip();
        Vector2f pos = MouseInputManager.getInstance().getPosition();

        myShip.direction = new Vector2f(pos).sub(new Vector2f(viewPort.width / 2, viewPort.height / 2)).normalise();
        client.getClient().sendTCP(new PlayerRotate(myShip.direction));
        
        
        //Move
        if(button == 1){
            SpacyClient.getInstance().getClient().sendTCP(new Move(EMoving.Accelerating,myShip.id));
            System.out.println("Start moving");
        }
        //Fire
        if(button == 0){
            //TODO Implement Weapon Cooldown
           
            SpacyClient.getInstance().getClient().sendTCP(
                    new Projectile(DamageType.balistic, myShip.id, myShip.direction, myShip.position));
        }
        client.replaceShip(myShip);
        
    }


    public Rect getViewPort() {
        return viewPort;
    }

    public void setViewPort(Rect viewPort) {
        this.viewPort = viewPort;
    }


    
}
