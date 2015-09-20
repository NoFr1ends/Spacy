package de.kryptondev.spacy;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.kryptondev.spacy.data.Ship;
import de.kryptondev.spacy.data.World;
import de.kryptondev.spacy.helper.KryoRegisterer;
import de.kryptondev.spacy.helper.UID;
import de.kryptondev.spacy.screen.Game;
import de.kryptondev.spacy.screen.ScreenManager;
import de.kryptondev.spacy.server.GameClient;
import de.kryptondev.spacy.share.Chatmessage;
import de.kryptondev.spacy.share.ConnectionAttemptResponse;
import de.kryptondev.spacy.share.PlayerInfo;
import de.kryptondev.spacy.share.Version;

public class SpacyClient extends Listener{

    private static SpacyClient instance;
    private Client client;
    private int port = 30300;
    private int timeout = 2500;
    public static final Version clientVersion = new Version(1, 0, 0);
    private PlayerInfo info;
    private World world;
    private Ship ship;


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

          
            client.addListener(this);
            client.start();
            client.connect(timeout, server, port);
            
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

    private void onRecv(Connection cnctn, Object o) {
        if (o instanceof ConnectionAttemptResponse) {
            //Antwort auswerten
            ConnectionAttemptResponse response = (ConnectionAttemptResponse) o;
            if (response.type == ConnectionAttemptResponse.Type.OK) {
                
                return;
            } else {
                this.client.close();
                connectionDropped(response.type);
                return;
            }

        }      
        if(o instanceof World){
            this.world=(World) o;
        }
        if(o instanceof Ship){
            this.ship = (Ship) o;   
            if(ScreenManager.getInstance().getCurrentScreen() instanceof Game){
                Game game = (Game)ScreenManager.getInstance().getCurrentScreen();
                game.onConnected();
            }
        }
        
        
        
        
        
        if(ScreenManager.getInstance().getCurrentScreen() instanceof Game){
            Game game = (Game)ScreenManager.getInstance().getCurrentScreen();
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
        System.out.println("Client is connected!");
        client.sendTCP(clientVersion);
        client.sendTCP(SpacyClient.this.info);
        //Recv World & Ship

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
        return ship;
    }

    public void setShip(Ship ship) {
        this.ship = ship;
    }
    
    
}
