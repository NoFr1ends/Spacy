package de.kryptondev.spacy.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import de.kryptondev.spacy.data.World;
import de.kryptondev.spacy.helper.KryoRegisterer;
import de.kryptondev.spacy.share.ConnectionAttemptResponse;
import de.kryptondev.spacy.share.Version;
import java.util.ArrayList;

public class SpacyServer extends Listener {
    public static SpacyServer instance;
    public volatile long EntityCounter = 0;
    private int maxSlots = 32;
    private int port = 30300;    
    private final int broadcastPort = 54777;
    private Server server;
    public static final Version serverVersion = new Version(1, 0, 0);
    private ArrayList<byte[]> bans;
    private ArrayList<byte[]> admins;
    public volatile World world;
    private GameTick tick;
    
    public void writeWarning(String s) {
        System.out.println(s);
    }

    public void writeInfo(String s) {
        System.out.println(s);
    }
    
    public void writeDebug(String s) {
        System.out.println(s);
    }

    public void writeError(String s) {
        System.err.println(s);
    }

    private void stdConstr() {           
        bans = new ArrayList<>();
        admins = new ArrayList<>();        
        instance = this;
        world = new World();
        server = new Server(port, broadcastPort){         
            @Override
            protected Connection newConnection() {
                return new GameClient(SpacyServer.this).instance;
            }
        
        };
        
    }

    public SpacyServer() {
        stdConstr();
    }

    public SpacyServer(int maxSlots) {
        if (maxSlots > 0) {
            this.maxSlots = maxSlots;
        }
        if (maxSlots == -1) {
            this.maxSlots = Integer.MAX_VALUE;
        }
        stdConstr();
    }

    public SpacyServer(int maxSlots, int port) {
        if (maxSlots > 0) {
            this.maxSlots = maxSlots;
        }
        if (maxSlots == -1) {
            this.maxSlots = Integer.MAX_VALUE;
        }

        if (port <= Short.MAX_VALUE & port > 0) {
            this.port = port;
        }
        stdConstr();
    }

    public boolean start() {
        try {
            KryoRegisterer.registerAll(server.getKryo());
            server.addListener(this);
            server.bind(port,broadcastPort);
            new Thread(server).start();       
            tick = new GameTick(this);
            Thread t = new Thread(tick);
            t.setName("Tick");
            t.start();
            
            writeInfo("Sever started.");            
          
        } catch (Exception ex) { 
            writeError(ex.getLocalizedMessage());
            writeError("Maybe a server is already running on this port?");
            return false;
        }
        
        
        
        return true;
    }
   

    public int getMaxSlots() {
        return maxSlots;
    }

    public Server getServer() {
        return server;
    }
  
    public GameClient getClientByName(String playerName) {
        for (GameClient.SGameClient item : (GameClient.SGameClient[])server.getConnections()) {
            if (item.getPlayerInfo().playerName.equals(playerName)) {
                return item.getGameClient();
            }
        }
        return null;
    }

    public GameClient getClientByUID(byte[] uid) {
        for (GameClient.SGameClient item : (GameClient.SGameClient[])server.getConnections()) {
            if (item.getPlayerInfo().playerUID == uid) {
                return item.getGameClient();
            }
        }
        return null;
    }

    public int getUsedSlots() {
        return server.getConnections().length;
    }

    public int getPort() {
        return port;
    }

    public boolean isPlayerBanned(byte[] uid) {
        ArrayList<byte[]> player = new ArrayList<byte[]>(1);
        player.add(uid);
        return bans.containsAll(player);
        //TODO: Implement hasGlobalBan
    }

    public boolean isPlayerAdmin(byte[] uid) {
        ArrayList<byte[]> player = new ArrayList<byte[]>(1);
        player.add(uid);
        return admins.containsAll(player);
    }

    @Override
    public void idle(Connection cnctn) {
        super.idle(cnctn);
    }

    @Override
    public void received(Connection cnctn, Object o) {
        //System.out.println("Server: Data was received!");
        //System.out.println(o.toString());
        GameClient.SGameClient gc = (GameClient.SGameClient)cnctn;
        gc.onRecv(o);          
    }

    @Override
    public void disconnected(Connection cnctn) {
        System.out.println("Server: Client is disconnected!");        
    }

    @Override
    public void connected(Connection cnctn) {        
        System.out.println("Server: Client is connected!");
        cnctn.sendTCP(new ConnectionAttemptResponse(ConnectionAttemptResponse.Type.OK));        
    }
    
    

}
