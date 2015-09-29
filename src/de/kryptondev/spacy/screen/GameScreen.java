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
    private final float backgroundMoveFactor = 0.5f;
    private Vector2f viewPortCenter = new Vector2f(0f, 0f);
    private Vector2f lastPos;
    private float zoom = 1.0f;
    private final float zoomStep = 0.5f;
    private final Random rand;
    private boolean debug = true;
    private float alphaWarn = 0f;
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
            
            width += gc.getWidth() / backgroundMoveFactor;
            height += gc.getHeight()/ backgroundMoveFactor;
            
            background = new Image(width, height);
            Graphics g = background.getGraphics();
            g.clear();
            int max = width * height / 800;
            
            for(int i = 0; i < max; i++){
                int rad = (int)(getRandomFloat(1.2f,5));                
                g.fillOval(rand.nextInt(width), rand.nextInt(height), rad, rad);
            }

            g.flush();            
        } catch (SlickException ex) {
            Logger.getLogger(GameScreen.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @Override
    public void update(GameContainer gc, int delta) {
        // Delete :)
        ArrayList<Long> toDelete = new ArrayList<>(SpacyClient.instance.toDelete);
        for(Long delete: toDelete) {
            client.getWorld().ships.remove(delete);
            client.getWorld().entities.remove(delete);
            client.getWorld().projectiles.remove(delete);
        }
        SpacyClient.instance.toDelete.clear();
        
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
            for(Projectile p : client.getWorld().projectiles.values()){
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
 
        if(isSpectatorMode()){   
            viewPortCenter = lastPos;            
            alphaWarn = 0f;
        }
        else
        {           
            Vector2f shipPos = client.getShip().position;
            lastPos = shipPos;
            this.viewPortCenter = shipPos;
            this.viewPort.x = (viewPortCenter.x - (this.viewPort.width / 2));
            this.viewPort.y = (viewPortCenter.y - (this.viewPort.height / 2));
            


            //"Redness"-control when leaving battle field
            float x = client.getShip().position.x;
            float y = client.getShip().position.y;
            int world = client.getWorld().worldSize;
            int tolerance = client.getWorld().toleranceDeathRadius;

            if(x < 0 | y < 0){
                if(x < y){
                    alphaWarn = Math.abs(x) / tolerance;
                }
                else{
                    alphaWarn = Math.abs(y) / tolerance;
                }
            }

            if(x > world | y > world){
                if(x > y){
                    alphaWarn = Math.abs(x - world) / tolerance;
                }
                else{
                    alphaWarn = Math.abs(y - world) / tolerance;
                }
            }
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
        g.clear();
        g.resetTransform();
        g.setBackground(BackgroundColor);
        
        g.translate((-viewPort.x) * backgroundMoveFactor - gc.getWidth() / backgroundMoveFactor / 2, (-viewPort.y) * backgroundMoveFactor - gc.getHeight() / backgroundMoveFactor / 2);
        background.draw(0, 0);
        
        g.resetTransform();
       
        //g.scale(zoom, zoom);
        g.translate((-viewPort.x) , (-viewPort.y));
        
        
        
        //Draw World Borders
        g.setLineWidth(32f);
        g.setColor(new Color(88,88,88, 180));
        g.drawRect(0, 0, client.getWorld().worldSize, client.getWorld().worldSize);
        g.setLineWidth(1f);
        if(client.getWorld() == null)
            return;
        
        
        
        try{
            ConcurrentHashMap<Long, Ship> ships = client.getWorld().ships;
            for(ConcurrentHashMap.Entry<Long, Ship> ship : ships.entrySet()){                 
                Vector2f renderPosition = ship.getValue().getCenteredRenderPos();            
                sheet.draw(ship.getValue().texture, 
                        renderPosition.x, 
                        renderPosition.y, 
                        ship.getValue().getRotation(),
                        ship.getValue().textureBounds.copy().scale(0.5f));
                
                if(this.debug){
                    drawCross(ship.getValue().position.x, ship.getValue().position.y, g);
                    ship.getValue().drawRotation(g);
                    ship.getValue().drawBounds(g);
                }
            }        
            ConcurrentHashMap<Long, Projectile> projectiles = client.getWorld().projectiles;
            for(ConcurrentHashMap.Entry<Long, Projectile> p : projectiles.entrySet()){
                
                Vector2f renderPosition = (p).getValue().getBulletRenderPos();
                sheet.draw(p.getValue().texture, 
                        renderPosition.x, 
                        renderPosition.y, p.getValue().getRotation(), null); 
                
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
            long objects = client.getWorld().projectiles.size() + client.getWorld().ships.size();
            
            g.resetTransform();
            g.setColor(Color.white);
            g.drawString("Serverdelta:     " + client.getServerTickDelta() + "ms", 8, 30);
            g.drawString("Ticks/s:         " + client.getServerTicksPerSecond(), 8, 50);            
            g.drawString("Viewport Pos:    " + this.viewPort.x + " | " + this.viewPort.y, 8, 70);
            g.drawString("Ship Pos:        " + this.viewPortCenter.x + " | " + this.viewPortCenter.y, 8, 90);
            g.drawString("Zoom:            " + this.zoom, 8, 110);
            g.drawString("Viewport Size:   " + this.viewPort.width + "x" + this.viewPort.height, 8, 130);
            g.drawString("Objects:         " + objects, 8, 150);
            g.drawString("alphaWarn:       " + alphaWarn, 8, 170);
        }
        
        g.resetTransform();
        g.setColor(new Color(0xff, 0x00, 0x00, alphaWarn));        
        g.fillRect(0, 0, gc.getWidth(), gc.getHeight());       
        
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
            if(!isSpectatorMode()){           
                Ship ship = client.getShip();  
                Vector2f pos = MouseInputManager.getInstance().getPosition();
                
                ship.direction = new Vector2f(pos).sub(new Vector2f(viewPort.width / 2, viewPort.height / 2)).normalise();
                client.setShip(ship);
                SpacyClient.getInstance().getClient().sendTCP(new PlayerRotate(client.getShip().direction));
            }
        }
        
    }
    
    @Override
    public void onButtonUp(int button) {
        //Move
        if(button == 1){
            if(!isSpectatorMode()){   
                SpacyClient.getInstance().getClient().sendTCP(new Move(EMoving.Deccelerating, client.getShip().id));
                System.out.println("Stop moving");
            }
        }
    }

    @Override
    public void onButtonPressed(int button) {
        //Rotate
        Ship myShip = client.getShip();
        if(isSpectatorMode()){   
            //Zuschauer-Modus
            
        }
        else{
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
    }


    public Rect getViewPort() {
        return viewPort;
    }

    public void setViewPort(Rect viewPort) {
        this.viewPort = viewPort;
    }

    public boolean isSpectatorMode(){
        return this.client.getShip() == null;
    }
    
}
