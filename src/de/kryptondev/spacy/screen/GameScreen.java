package de.kryptondev.spacy.screen;

import de.kryptondev.spacy.data.*;
import de.kryptondev.spacy.share.Move;
import de.kryptondev.spacy.share.PlayerRotate;
import de.kryptondev.spacy.SpacyClient;
import de.kryptondev.spacy.SpriteSheet;
import de.kryptondev.spacy.input.KeyInputManager;
import de.kryptondev.spacy.input.MouseInputManager;
import de.kryptondev.spacy.share.PlayerInfo;
import java.awt.Font;
import java.util.ArrayList;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.management.timer.Timer;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.Input;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.TrueTypeFont;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class GameScreen implements IScreen, KeyInputManager.KeyListener, MouseInputManager.MouseListener {
    public static final org.newdawn.slick.Color BackgroundColor = new org.newdawn.slick.Color(8,8,64);
    private SpacyClient client;
    private Image background;
    private TrueTypeFont font;
    private TrueTypeFont scoreFont;
    private Rect viewPort;
    private final float backgroundMoveFactor = 0.5f;
    private Vector2f viewPortCenter = new Vector2f(0f, 0f);
    private Vector2f lastPos;
    private float zoom = 1.0f;
    private final float zoomStep = 0.5f;
    private final Random rand;
    private boolean debug = false;
    private boolean showScoreboard = false;
    private float alphaWarn = 0f;
    private SpriteSheet sheet;
    private ScheduledExecutorService cooldown;
    private boolean ready = false;
    private int delta;
    private boolean canShoot = true;
    private int paneLenght = 512;
    private Vector2f backgroundBasePos = new Vector2f();
    private int panePos = -1;
    public GameScreen() {
        this.client = SpacyClient.getInstance();
        this.rand = new Random();      
    }
    
    public float getRandomFloat(float min, float max){
        return rand.nextFloat() * (max - min) + min;
    }
    
    @Override
    public void init(GameContainer gc) {    
        MouseInputManager.getInstance().registerListener("Fire", Input.MOUSE_LEFT_BUTTON, this);       
        MouseInputManager.getInstance().registerListener("Throttle", Input.MOUSE_RIGHT_BUTTON, this);
        
        KeyInputManager.getInstance().registerListener("Debug", Input.KEY_F12, this);
        KeyInputManager.getInstance().registerListener("showScorboard", Input.KEY_TAB, this);
        
        KeyInputManager.getInstance().registerListener("Zoom In", Input.KEY_ADD, this);        
        KeyInputManager.getInstance().registerListener("Zoom Out", Input.KEY_SUBTRACT, this);
        KeyInputManager.getInstance().registerListener("Zoom Reset", Input.KEY_NUMPAD0, this);

        Random r = new Random();
        gc.setAlwaysRender(true);
        
        Font awtFont = new Font("Arial", Font.BOLD, 20);
        font = new TrueTypeFont(awtFont, true);
        
        //Adjust fontsize if strings are displayed weirdly
        int fontsize;
        if (gc.getHeight()>=1080)
            fontsize= 20;
        else if (gc.getHeight()>=768)
            fontsize= 15;
        else if (gc.getHeight()>=600)
            fontsize= 12;
        else
            fontsize = 10;
        Font scoreAwtFont = new Font("Arial", Font.PLAIN, fontsize);
        scoreFont = new TrueTypeFont(scoreAwtFont, true);
        
        // Load main sprite sheet
        sheet = new SpriteSheet("data/sheet.xml");
        
        try {       
            viewPort = new Rect(0, 0, gc.getWidth(), gc.getHeight());
            paneLenght = gc.getWidth() > gc.getHeight() ? gc.getWidth() : gc.getHeight();
            
            int width =paneLenght;
            int height = paneLenght;
            
         
            
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
        ready = true;
        
        this.cooldown = Executors.newScheduledThreadPool(1);
       
        
    }
    
    private Vector2f getCurrentPane(){
        return new Vector2f((float)((int)viewPort.x / paneLenght),
            (float)((int)viewPort.y / paneLenght));
    }
    
    private int getPartipalCurrentPane(){
        if(viewPort == null)
            return -1;
        int w = (int)viewPort.x % paneLenght;
        int h = (int)viewPort.y % paneLenght;
        
        if(h < paneLenght / 2){
            //Oben
            if(w < paneLenght / 2){
                return 1;
            }
            else{
                return 2;
            }
        }
        else{
            //Unten
            if(w < paneLenght / 2){
                return 3;
            }
            else{
                return 4;
            }
        }
        
    }

    public void startCooldown(int cooldownMilliseconds){
        this.canShoot = false;
        cooldown.schedule(new Runnable() {

            @Override
            public void run() {
                GameScreen.this.canShoot = true;
            }
        }, cooldownMilliseconds, TimeUnit.MILLISECONDS);
    }
    
    @Override
    public void update(GameContainer gc, int delta) {
        this.delta = delta;
        
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
                if (ship.moving != EMoving.Stopped)
                    ship.move(delta);
                
                if(ship.moving != EMoving.Stopped)
                    System.out.println("Client:[" + ((System.nanoTime()/1000000)-ship.movementChangedTime) + "] Ship " + ship.id + " is moving! " + ship.position + " (delta: " + delta + ", state=" + ship.moving + ")");
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        try{
            for(Projectile p : client.getWorld().projectiles.values()){
                if (p.moving != EMoving.Stopped)
                    p.move(delta);
            }
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        try{
            ConcurrentHashMap<Long, Entity> entities = new ConcurrentHashMap<>(client.getWorld().entities);
            for(Entity e : entities.values()){
                if (e.moving != EMoving.Stopped)
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
            
            backgroundBasePos = getCurrentPane();
            panePos = getPartipalCurrentPane();


            //"Redness"-control when leaving battle field
            float x = client.getShip().position.x;
            float y = client.getShip().position.y;
            int world = client.getWorld().worldSize;
            int tolerance = client.getWorld().toleranceDeathRadius;

            boolean alphaChanged = false;
            
            if(x < 0 | y < 0){
                alphaChanged = true;
                if(x < y){
                    alphaWarn = Math.abs(x) / tolerance;
                }
                else{
                    alphaWarn = Math.abs(y) / tolerance;
                }
            }
            
            if(x > world | y > world){
                alphaChanged = true;
                if(x > y){
                    alphaWarn = Math.abs(x - world) / tolerance;
                }
                else{
                    alphaWarn = Math.abs(y - world) / tolerance;
                }
            }
            
            if(!alphaChanged){
                alphaWarn = 0f;
            }
            
            
            
        }
        
    }
    
    public void drawCross(Vector2f pos, Graphics g, long vid){
      
        float size = 50f;
        g.setColor(Color.magenta);
        g.drawLine(pos.x - size / 2, pos.y, pos.x + size / 2, pos.y);
        g.drawLine(pos.x, pos.y - size/ 2, pos.x, pos.y +  size / 2);
        g.setColor(Color.white);
        g.drawString(pos.x + " | " + pos.y , pos.x + 10, pos.y - 10);  
        g.drawString("ID: "+vid, pos.x + 10, pos.y - 30);
    }
    
    public void drawCross(float x, float y, Graphics g, long vid){
        drawCross(new Vector2f(x,y),g, vid);
    }
    
    @Override
    public void draw(GameContainer gc, Graphics g) {    
        g.clear();
        g.resetTransform();
        g.setBackground(BackgroundColor);
        g.translate(backgroundBasePos.getX() * paneLenght- viewPort.x, backgroundBasePos.getY() * paneLenght- viewPort.y);        
        //Current
        background.draw();   
        //Right
        background.draw(paneLenght, 0);
        //Bottom
        background.draw(0, paneLenght);
        //Bottm-Right
        background.draw(paneLenght, paneLenght);       
        
        if(viewPort.x < 0){
            background.draw(-paneLenght, 0);
        }
        
        if(viewPort.y < 0){
            background.draw(0, -paneLenght);
        }
        
        if(viewPort.x < 0 & viewPort.y < 0){
            background.draw(-paneLenght, -paneLenght);
        }
        
        if(debug){            
            g.setLineWidth(1f);
            Color c = new Color((float)0xff, (float)0x66, 0f, 255f);
            g.setColor(c);
            g.drawLine(paneLenght, 0, paneLenght, paneLenght * 2);        
            g.drawLine( 0,paneLenght, paneLenght * 2, paneLenght);    
        }
        
        g.resetTransform();
        g.translate((-viewPort.x) , (-viewPort.y));      
        
        
        //Draw World Borders
        g.setLineWidth(32f);
        g.setColor(new Color(88,88,88, 180));
        g.drawRect(0, 0, client.getWorld().worldSize, client.getWorld().worldSize);
        g.setLineWidth(1f);
        if(client.getWorld() == null)
            return;
        
        
        try{            
            //DrawShips & Player Names
            ConcurrentHashMap<Long, Ship> ships = client.getWorld().ships;
            for(ConcurrentHashMap.Entry<Long, Ship> ship : ships.entrySet()){                 
                Vector2f renderPosition = ship.getValue().getCenteredRenderPos();            
                sheet.draw(ship.getValue().texture, 
                        renderPosition.x, 
                        renderPosition.y, 
                        ship.getValue().getRotation(),
                        ship.getValue().textureBounds.copy().scale(0.5f));
                
                font.drawString(
                        ship.getValue().position.x - font.getWidth(client.getWorld().players.get(ship.getValue().owner).playerName) / 2, 
                        ship.getValue().position.y - 50, client.getWorld().players.get(ship.getValue().owner).playerName, 
                        Color.white);
                if(this.debug){
                    drawCross(ship.getValue().position.x, ship.getValue().position.y, g, ship.getValue().id);
                    ship.getValue().drawRotation(g);
                    ship.getValue().drawBounds(g);
                }
            }      
            //Draw Projectiles
            ConcurrentHashMap<Long, Projectile> projectiles = client.getWorld().projectiles;
            for(ConcurrentHashMap.Entry<Long, Projectile> p : projectiles.entrySet()){                
                Vector2f renderPosition = (p).getValue().getCenteredRenderPos();
                sheet.draw(p.getValue().texture, 
                        renderPosition.x, 
                        renderPosition.y, p.getValue().getRotation(), null); 
                
                if(this.debug){
                    drawCross(p.getValue().position.x, p.getValue().position.y, g, p.getValue().id);
                    p.getValue().drawRotation(g);
                    p.getValue().drawBounds(g);
                }
            }        
        }
        catch(Exception ex){
            ex.printStackTrace();
        }
        
        
        // HUD
        
        g.resetTransform();
        g.setColor(Color.white);
        g.setLineWidth(15);
        g.drawLine(20, 20, 220, 20);
        
        g.setColor(Color.red);
        if(client.getShip() != null)
            g.drawLine(20, 20, client.getShip().hp * 2 + 20, 20);
        
        
        //SCOREBOARD
        
        if (showScoreboard){
            g.resetTransform();
           
            g.setColor(Color.darkGray);
            Rectangle ScoreBoard = new Rectangle(gc.getWidth()/6, gc.getHeight()/6,gc.getWidth()/6*4,gc.getHeight()/6*4);
            g.fill(ScoreBoard);
            font.drawString((gc.getWidth() - font.getWidth("SCOREBOARD")) / 2, 
                 ScoreBoard.getY()+ScoreBoard.getHeight()*0.025f,
                 "SCOREBOARD", Color.white);
            g.setColor(Color.lightGray);
            g.setLineWidth(1);
            
            g.drawLine(ScoreBoard.getX()+ ScoreBoard.getWidth()*0.05f,
                    ScoreBoard.getY()+font.getHeight("SCOREBOARD")+ScoreBoard.getHeight()*0.045f,
                    ScoreBoard.getMaxX()-ScoreBoard.getWidth()*0.05f,
                    ScoreBoard.getY()+font.getHeight("SCOREBOARD")+ScoreBoard.getHeight()*0.045f);
            
            Rectangle Scores = new Rectangle(ScoreBoard.getX(),
                    ScoreBoard.getY()+font.getHeight("SCOREBOARD")+ScoreBoard.getHeight()*0.1f,
                    ScoreBoard.getWidth(),
                    ScoreBoard.getHeight()-font.getHeight("SCOREBOARD")*0.9f-ScoreBoard.getHeight()*0.15f);
            
            if(false)//ADD TDM CHECK
            {
                float cellWidth = ScoreBoard.getWidth()*0.4f;
                float cellHeight = (ScoreBoard.getHeight()-ScoreBoard.getHeight()*0.05f*17)/16;

                scoreFont.drawString( Scores.getX()+ Scores.getWidth()*0.05f + cellWidth*0.3f -scoreFont.getWidth("Name")/2, Scores.getY(), "Name", Color.blue);
                scoreFont.drawString( Scores.getX()+ Scores.getWidth()*0.05f + cellWidth*0.6f + cellWidth*0.1f -scoreFont.getWidth("Kills")/2, Scores.getY(), "Kills", Color.blue);
                scoreFont.drawString( Scores.getX() + Scores.getWidth()*0.05f + cellWidth*0.8f + cellWidth*0.1f -scoreFont.getWidth("Deaths")/2, Scores.getY(), "Deaths", Color.blue);

                scoreFont.drawString( Scores.getX()+ Scores.getWidth()*0.05f + cellWidth + Scores.getWidth()*0.05f + Scores.getWidth()*0.05f + cellWidth*0.3f -scoreFont.getWidth("Name")/2, Scores.getY(), "Name", Color.red);
                scoreFont.drawString( Scores.getX()+ Scores.getWidth()*0.05f + cellWidth + Scores.getWidth()*0.05f + Scores.getWidth()*0.05f + cellWidth*0.6f + cellWidth*0.1f -scoreFont.getWidth("Kills")/2, Scores.getY(), "Kills", Color.red);
                scoreFont.drawString( Scores.getX()+ Scores.getWidth()*0.05f + cellWidth + Scores.getWidth()*0.05f + Scores.getWidth()*0.05f + cellWidth*0.8f + cellWidth*0.1f -scoreFont.getWidth("Deaths")/2, Scores.getY(), "Deaths", Color.red);

                if (debug)
                {
                    g.drawLine(Scores.getX()+ Scores.getWidth()*0.05f,
                            Scores.getY()+Scores.getHeight()*0.05f,
                            Scores.getX() + Scores.getWidth()*0.05f,
                            Scores.getMaxY()-Scores.getHeight()*0.05f);

                    g.drawLine(Scores.getX()+ Scores.getWidth()*0.05f + cellWidth*0.6f,
                            Scores.getY()+Scores.getHeight()*0.05f,
                            Scores.getX() + Scores.getWidth()*0.05f + cellWidth*0.6f,
                            Scores.getMaxY()-Scores.getHeight()*0.05f);

                    g.drawLine(Scores.getX()+ Scores.getWidth()*0.05f + cellWidth*0.8f,
                            Scores.getY()+Scores.getHeight()*0.05f,
                            Scores.getX() + Scores.getWidth()*0.05f + cellWidth*0.8f,
                            Scores.getMaxY()-Scores.getHeight()*0.05f);

                    g.drawLine(Scores.getX()+ Scores.getWidth()*0.05f + cellWidth,
                            Scores.getY()+Scores.getHeight()*0.05f,
                            Scores.getX() + Scores.getWidth()*0.05f + cellWidth,
                            Scores.getMaxY()-Scores.getHeight()*0.05f);

                    g.drawLine(Scores.getX()+ Scores.getWidth()*0.05f + cellWidth + Scores.getWidth()*0.05f + Scores.getWidth()*0.05f,
                            Scores.getY()+Scores.getHeight()*0.05f,
                            Scores.getX() + Scores.getWidth()*0.05f + cellWidth + Scores.getWidth()*0.05f + Scores.getWidth()*0.05f,
                            Scores.getMaxY()-Scores.getHeight()*0.05f);

                    g.drawLine(Scores.getX()+ Scores.getWidth()*0.05f + cellWidth + Scores.getWidth()*0.05f + Scores.getWidth()*0.05f + cellWidth*0.6f,
                            Scores.getY()+Scores.getHeight()*0.05f,
                            Scores.getX() + Scores.getWidth()*0.05f + cellWidth + Scores.getWidth()*0.05f + Scores.getWidth()*0.05f + cellWidth*0.6f,
                            Scores.getMaxY()-Scores.getHeight()*0.05f);

                    g.drawLine(Scores.getX()+ Scores.getWidth()*0.05f + cellWidth + Scores.getWidth()*0.05f + Scores.getWidth()*0.05f + cellWidth*0.8f,
                            Scores.getY()+Scores.getHeight()*0.05f,
                            Scores.getX() + Scores.getWidth()*0.05f + cellWidth + Scores.getWidth()*0.05f + Scores.getWidth()*0.05f + cellWidth*0.8f,
                            Scores.getMaxY()-Scores.getHeight()*0.05f);

                    g.drawLine(Scores.getX()+ Scores.getWidth()*0.05f + cellWidth + Scores.getWidth()*0.05f + Scores.getWidth()*0.05f + cellWidth,
                            Scores.getY()+Scores.getHeight()*0.05f,
                            Scores.getX() + Scores.getWidth()*0.05f + cellWidth + Scores.getWidth()*0.05f + Scores.getWidth()*0.05f + cellWidth,
                            Scores.getMaxY()-Scores.getHeight()*0.05f);
                }

                g.drawLine(Scores.getX()+ Scores.getWidth()*0.05f + cellWidth + Scores.getWidth()*0.05f,
                        Scores.getY()+Scores.getHeight()*0.05f,
                        Scores.getX() + Scores.getWidth()*0.05f + cellWidth + Scores.getWidth()*0.05f,
                        Scores.getMaxY()-Scores.getHeight()*0.05f);


                try{
                    World world = client.getWorld();
                    ConcurrentHashMap<Long, Ship> ships = world.ships;
                    
                    for(ConcurrentHashMap.Entry<Long, Ship> ship : ships.entrySet()){
                        //Todo TDM Team Management
                        
                    }
                }
                catch (Exception ex){
                    throw ex;
                }
            }
            else
            {
                float cellWidth = ScoreBoard.getWidth()*0.9f;
                float cellHeight = ScoreBoard.getHeight()/32;

                scoreFont.drawString( Scores.getX()+ Scores.getWidth()*0.05f + cellWidth*0.3f -scoreFont.getWidth("Name")/2, Scores.getY(), "Name");
                scoreFont.drawString( Scores.getX()+ Scores.getWidth()*0.05f + cellWidth*0.6f + cellWidth*0.1f -scoreFont.getWidth("Kills")/2, Scores.getY(), "Kills");
                scoreFont.drawString( Scores.getX() + Scores.getWidth()*0.05f + cellWidth*0.8f + cellWidth*0.1f -scoreFont.getWidth("Deaths")/2, Scores.getY(), "Deaths");
                
                
                try{
                    World world = client.getWorld();
                    ConcurrentHashMap<Long, Ship> ships = world.ships;
                    float posY = Scores.getY()+Scores.getHeight()*0.05f;
                    boolean grey=false;
                    Color fontColor = Color.white;
                    
                    for(ConcurrentHashMap.Entry<Long, Ship> ship : ships.entrySet()){
                        if (grey)
                        {
                            g.setColor(Color.lightGray);
                            g.fillRect(Scores.getX()+ Scores.getWidth()*0.05f, posY+ cellHeight*0.1f, cellWidth, cellHeight*1.1f);
                            fontColor = Color.black;
                        }
                        else
                            fontColor = Color.white;
                        
                        PlayerInfo player = world.players.get(ship.getValue().owner);
                        String name = player.playerName;
                        int kills = player.kills;
                        int deaths = player.deaths;
                        
                        scoreFont.drawString( Scores.getX()+ Scores.getWidth()*0.05f + cellWidth*0.3f -scoreFont.getWidth(name)/2, posY, name, fontColor);
                        scoreFont.drawString( Scores.getX()+ Scores.getWidth()*0.05f + cellWidth*0.6f + cellWidth*0.1f -scoreFont.getWidth(Integer.toString(kills))/2, posY, Integer.toString(kills), fontColor);
                        scoreFont.drawString( Scores.getX() + Scores.getWidth()*0.05f + cellWidth*0.8f + cellWidth*0.1f -scoreFont.getWidth(Integer.toString(deaths))/2, posY, Integer.toString(deaths), fontColor);
                        
                        posY+=cellHeight+ cellHeight*0.05f;
                        grey = !grey;
                        if (posY>Scores.getMaxY())
                            break;
                    }
                }
                catch (Exception ex){
                    throw ex;
                } 
            }
        }
        
        
        /*
        DEBUG SECTION
        */
        if(this.debug){
            long objects = client.getWorld().projectiles.size() + client.getWorld().ships.size();
            
            g.resetTransform();
            g.setColor(Color.white);
            
            g.drawString("Clientdelta:      " + this.delta + "ms", 8, 30);
            g.drawString("Serverdelta:      " + client.getServerTickDelta() + "ms", 8, 50);
            g.drawString("Ticks/s:          " + client.getServerTicksPerSecond(), 8, 70);            
            g.drawString("Viewport Pos:     " + this.viewPort.x + " | " + this.viewPort.y, 8, 90);
            if(client.getShip() != null) 
                g.drawString("Ship Pos:         " + client.getShip().position.toString() , 8, 110);
            g.drawString("Zoom:             " + this.zoom, 8, 130);
            g.drawString("Viewport Size:    " + this.viewPort.width + "x" + this.viewPort.height, 8, 150);
            g.drawString("Objects:          " + objects + "(" + client.getWorld().ships.size() + ")", 8, 170);
            g.drawString("alphaWarn:        " + alphaWarn, 8, 190);
            g.drawString("Corner:           " + getPartipalCurrentPane(), 8, 210);
            g.drawString("BackgroundBasePos " + getCurrentPane(), 8, 230);
            if(client.getShip() != null)
                g.drawString("VID:             " + client.getShipId() + " (" + client.getShip().hashCode() + ")", 8, 250);
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
      if (key == Input.KEY_TAB)
          showScoreboard = true;
    }

    @Override
    public void onKeyUp(int key) {
      if (key == Input.KEY_TAB)
          showScoreboard = false;
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
    @Override
    public void onButtonDown(int button) {
        if(button == 1){           
            if(!isSpectatorMode()){           
                Ship ship = client.getShip();  
                Vector2f pos = MouseInputManager.getInstance().getPosition();
                
                ship.direction = new Vector2f(pos).sub(new Vector2f(viewPort.width / 2, viewPort.height / 2)).normalise();
                //client.setShip(ship);
                SpacyClient.getInstance().getClient().sendTCP(new PlayerRotate(client.getShip().direction));
            }
        }
        
    }
    
    @Override
    public void onButtonUp(int button) {
        //Move
        if(button == 1){
            if(!isSpectatorMode()){   
                client.getShip().moving = EMoving.Deccelerating;
                long movementChangedTime = System.nanoTime() / 1000000;
                SpacyClient.getInstance().getClient().sendTCP(new Move(EMoving.Deccelerating, client.getShip().id, movementChangedTime, client.getShip().position));
                //System.out.println("Stop moving");
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
                long movementChangedTime = System.nanoTime() / 1000000;
                SpacyClient.getInstance().getClient().sendTCP(new Move(EMoving.Accelerating,myShip.id, movementChangedTime, myShip.position));
                //System.out.println("Start moving");
            }
            //Fire
            if(button == 0 && this.canShoot){
                System.out.println("Ship " + myShip.id + " - Projectile " + myShip.activeWeapon.ammo.id);
                SpacyClient.getInstance().getClient().sendTCP(myShip.activeWeapon.ammo);
                startCooldown(myShip.activeWeapon.cooldown);
            }
            
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

    public boolean isReady() {
        return ready;
    }
    
    
    
}
