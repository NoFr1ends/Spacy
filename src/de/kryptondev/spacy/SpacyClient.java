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
import de.kryptondev.spacy.share.playerEvents.OnJoin;
import java.lang.reflect.Field;


public class SpacyClient extends Listener{

    public static SpacyClient instance;
    private Client client;
    private final int port = 30300;
    private int timeout = 2500;
    public static final Version clientVersion = new Version(1, 0, 0);
    private PlayerInfo info;
    private World world = new World();
    private long shipId;
    private int serverTickDelta;
    private int serverTicksPerSecond;
    
    public SpacyClient() {
        this.info = new PlayerInfo();
        info.OS = System.getProperty("os.name");
        info.playerUID = UID.getUID();

        //For test pourpose
        info.playerName = System.getProperty("user.name");

    }   
   

    public void connect(String server) {
        try {
            client = new Client();
            KryoRegisterer.registerAll(client.getKryo());
            new Thread(client).start();
            client.addListener(this);
            client.connect(timeout, server, port, 54777);

        } catch (Exception ex) {
             System.err.println(ex.getLocalizedMessage());
            //TODO Handle error
        }
    }

    private void connectionDropped(ConnectionAttemptResponse.Type reason) {
        //GUI Update
        System.out.println("Client is not connected! Reason: " + reason.toString());
    }

    public void sendMessage(String message) {
        Chatmessage msg = new Chatmessage(message);
        msg.sender = this.info.playerName;
        this.client.sendTCP(msg);
    }

    private void logPacket(Object o) {
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
                this.world.ships.replace(shipId, s);
            }
        }
        
        if(o instanceof Move){
            Move move = (Move)o;
            Ship ship = this.world.ships.get(move.id);
            ship.moving = move.status;
            this.world.ships.replace(shipId, ship);
        }
        
        if (o instanceof ConnectionAttemptResponse) {
            //Antwort auswerten
            ConnectionAttemptResponse response = (ConnectionAttemptResponse) o;
            if (response.type == ConnectionAttemptResponse.Type.OK) {
                this.world.worldSize = response.worldSize;
                   if(ScreenManager.getInstance().getCurrentScreen() instanceof GameScreen){
                        GameScreen game = (GameScreen)ScreenManager.getInstance().getCurrentScreen();
                        game.onConnected();
                    }
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
            
            if(ScreenManager.getInstance().getCurrentScreen() instanceof GameScreen){
                GameScreen game = (GameScreen)ScreenManager.getInstance().getCurrentScreen();
                game.setMyShip(s);
                shipId = s.id;
            }
            
            world.ships.put(s.id ,s);
            
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
            if(world.projectiles.containsKey(entity.vid)) {
                world.projectiles.remove(entity.vid);
            }
            else if(world.ships.containsKey(entity.vid)) {
                world.ships.remove(entity.vid);
            }
            else if(world.entities.containsKey(entity.vid)) {
                world.entities.remove(entity.vid);
            }
        }
        
        if(o instanceof OnJoin){
            OnJoin join = (OnJoin)o;
            this.world.ships.put(join.ship.id, join.ship);
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
        return this.world.ships.getOrDefault(this.shipId, null);
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
    
    
    
}
