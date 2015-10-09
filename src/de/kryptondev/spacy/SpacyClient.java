package de.kryptondev.spacy;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;

import de.kryptondev.spacy.data.*;
import de.kryptondev.spacy.share.*;

import de.kryptondev.spacy.helper.KryoRegisterer;
import de.kryptondev.spacy.helper.UID;

import de.kryptondev.spacy.screen.GameScreen;
import de.kryptondev.spacy.screen.ScreenManager;
import de.kryptondev.spacy.screen.WaitingScreen;
import de.kryptondev.spacy.share.playerEvents.OnJoin;
import java.lang.reflect.Field;
import java.util.ArrayList;
import org.omg.PortableServer.THREAD_POLICY_ID;


public class SpacyClient extends Listener{

    public static SpacyClient instance;
    private Client client;
    private final int port = 30300;
    private int timeout = 2500;
    public static final Version clientVersion = new Version(1, 0, 0);
    private PlayerInfo info;
    private World world = new World();
    private long shipId = 0;
    private int serverTickDelta;
    private int serverTicksPerSecond;
    private WaitingScreen waiting;
    public ArrayList<Long> toDelete = new ArrayList<>();
    
    public SpacyClient() {
        this.info = new PlayerInfo();
        info.OS = System.getProperty("os.name");
        info.playerUID = UID.getUID();

        //For test pourpose
        info.playerName = System.getProperty("user.name");
        
    }   
   

    public void connect(String server, WaitingScreen waiting) {
        try {
            ScreenManager.getInstance().changeScreen(waiting);
            this.waiting = waiting;
            waiting.setStatusMessage("Initializing client");
            client = new Client();
            KryoRegisterer.registerAll(client.getKryo());
            new Thread(client).start();
            client.addListener(this);
            waiting.setStatusMessage("Connecting...");
            client.connect(timeout, server, port, 54777);

        } catch (Exception ex) {
             System.err.println(ex.getLocalizedMessage());
             waiting.setStatusMessage("Error: "+ex.getLocalizedMessage());
        }
    }

    private void connectionDropped(ConnectionAttemptResponse.Type reason) {
        System.out.println("Client is not connected! Reason: " + reason.toString());
        waiting.setStatusMessage("Client is not connected! Reason: " + reason.toString());
        ScreenManager.getInstance().changeScreen(waiting);        
    }

    public void sendMessage(String message) {
        Chatmessage msg = new Chatmessage(message);
        msg.sender = this.info.playerName;
        this.client.sendTCP(msg);
    }

    private void logPacket(Object o) {
        if(o instanceof DebugTickDelta)
            return;
        if(o instanceof PlayerRotate)
            return;
        System.out.print("Recv: " + o.getClass().getSimpleName() + "(");
        
        for(Field f: o.getClass().getFields()) {
            try {
                System.out.print(f.getName() + "=" + f.get(o) + " ");
            } catch(Exception e) {
                System.out.print(f.getName() + "=error ");
            }
        }
        
        System.out.println(")");
    }
    
    private void onRecv(Connection cnctn, Object o) {
        logPacket(o);
        
        if(o instanceof PlayerRotate){
            PlayerRotate r = (PlayerRotate)o;
            if(this.world.ships.containsKey(r.ship)){
                Ship s = this.world.ships.get(r.ship);
                s.direction = r.direction;
                //this.world.ships.put(shipId, s);
            }
        }
        
        if(o instanceof Move){
            Move move = (Move)o;
            Ship ship = this.world.ships.get(move.id);
            if(ship != null){
                if (move.status != EMoving.FullSpeed && move.status != EMoving.Stopped){
                        ship.movementChangedTimeOld = ship.movementChangedTime;
                        ship.movementChangedTime = move.movementChangedTime+16;
                        long currTime = System.nanoTime()/1000000;
                        System.out.println(currTime + " - " + ship.movementChangedTimeOld);
                        if (currTime-ship.movementChangedTimeOld < ship.acceleration && (ship.movementChangedTimeOld-ship.movementChangedTime)<500)
                            ship.movementChangedTime -= ship.acceleration -(System.nanoTime()/1000000 - ship.movementChangedTimeOld);
                }
                ship.moving = move.status;
                ship.position = move.pos;
                //this.world.ships.put(shipId, ship);
            }
            else{
                System.err.println("Ship " + move.id + " not found!");
            }
                
        }
        
        if (o instanceof ConnectionAttemptResponse) {
            //Antwort auswerten
            ConnectionAttemptResponse response = (ConnectionAttemptResponse) o;
            if (response.type == ConnectionAttemptResponse.Type.OK) {
                GameScreen gs = new GameScreen();
                ScreenManager.getInstance().changeScreen(gs);
                this.world.worldSize = response.worldSize;
                gs.onConnected();
                    
                return;
            } else {
                this.client.close();
                connectionDropped(response.type);
                return;
            }

        }     
        
        if(o instanceof ChunkedShip){
            this.world.ships.putAll(((ChunkedShip)o).ships);         
        }
        
        if(o instanceof ChunkedEntity){
            this.world.entities.putAll(((ChunkedEntity)o).entities);         
        }
        
         if(o instanceof ChunkedProjectiles){
            this.world.projectiles.putAll(((ChunkedProjectiles)o).projectiles);         
        }
         
        if(o instanceof Ship){
            Ship s = (Ship)o;
            //SPAWNED
            System.out.println("SHIP RECEIVED!!!");
            world.ships.put(s.id ,s);
            shipId = s.id;       
            this.info = s.owner;
            return;
        }
        
        
        if(o instanceof Projectile){
            Projectile p = (Projectile)o;
            this.world.projectiles.put(p.id, p);            
        }
        
        if(o instanceof DebugTickDelta){
            DebugTickDelta delta = ((DebugTickDelta)o);
            this.serverTickDelta = delta.tickDelta;
            this.serverTicksPerSecond = delta.ticksPerSecond;
        }
        
        if(o instanceof DeleteEntity){
            DeleteEntity entity = (DeleteEntity)o;
            
            toDelete.add(entity.vid);
        }
        
        if(o instanceof OnJoin){
            OnJoin join = (OnJoin)o;
            this.world.ships.put(join.ship.id, join.ship);
        }
        
        if(o instanceof World){
             this.world = (World)o;   
        }
        
        if(o instanceof UpdateLife) {
            UpdateLife life = (UpdateLife) o;
            getShip().hp = life.hp;
        }
         
        if(ScreenManager.getInstance().getCurrentScreen() instanceof GameScreen){
            GameScreen game = (GameScreen)ScreenManager.getInstance().getCurrentScreen();
            game.onRecv(o);
        }
    }

    
    
    @Override
    public void received(Connection cnctn, Object o) {        
        onRecv(cnctn, o);
    }

    @Override
    public void disconnected(Connection cnctn) {
        connectionDropped(ConnectionAttemptResponse.Type.Disconnect);
    }

    @Override
    public void connected(Connection cnctn) {  
        waiting.setStatusMessage("Sending data");
        client.sendTCP(clientVersion);       
        client.sendTCP(SpacyClient.this.info);
    }
                
                
    public static SpacyClient getInstance() {
        return instance;
    }

    public static void setInstance(SpacyClient instance) {
        SpacyClient.instance = instance;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }

    public PlayerInfo getInfo() {
        return info;
    }

    public void setInfo(PlayerInfo info) {
        this.info = info;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public Ship getShip() {
        return this.world.ships.get(this.shipId);
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public int getPort() {
        return port;
    }

    public long getShipId() {
        return shipId;
    }

    public int getServerTickDelta() {
        return serverTickDelta;
    }

    public int getServerTicksPerSecond() {
        return serverTicksPerSecond;
    }

    public void setShipId(long shipId) {
        this.shipId = shipId;
    }    
    
}
