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
import java.lang.reflect.Field;


public class SpacyClient extends Listener{

    public static SpacyClient instance;
    private Client client;
    private final int port = 30300;
    private int timeout = 2500;
    public static final Version clientVersion = new Version(1, 0, 0);
    private PlayerInfo info;
    private World world;
    private long shipId;
    
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
        
        if (o instanceof ConnectionAttemptResponse) {
            //Antwort auswerten
            ConnectionAttemptResponse response = (ConnectionAttemptResponse) o;
            if (response.type == ConnectionAttemptResponse.Type.OK) {
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
            this.world.ships.addAll(((ChunkedShip)o).ships);         
        }
        
        if(o instanceof ChunkedEntity){
            this.world.entities.addAll(((ChunkedEntity)o).entities);         
        }
        
         if(o instanceof ChunkedProjectiles){
            this.world.projectiles.addAll(((ChunkedProjectiles)o).projectiles);         
        }
         
        if(o instanceof Ship){
            Ship s = (Ship)o;
              
            if(ScreenManager.getInstance().getCurrentScreen() instanceof GameScreen){
                GameScreen game = (GameScreen)ScreenManager.getInstance().getCurrentScreen();
                game.setMyShip(s);
                shipId = s.id;
            }
            
            world.ships.add(s);
            //SPAWNED
            //setShip(s);
            return;
        }
        
        if(o instanceof PlayerConnectionEvent){
            
        }
        
        if(o instanceof Projectile){
            this.world.projectiles.add((Projectile)o);
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
        for(Ship ship : world.ships){
            if(ship.id == this.shipId){
                return ship;
            }
        }
        return null;
    }

    @Deprecated
    public void setShip(Ship ship) {
         for(Ship wShip : world.ships){
            if(wShip.id == this.shipId){
                wShip = ship;
            }
        }
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }
    
    
}
