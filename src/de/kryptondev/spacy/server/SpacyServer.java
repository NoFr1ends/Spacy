package de.kryptondev.spacy.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.sun.media.jfxmediaimpl.MediaDisposer.Disposable;
import de.kryptondev.spacy.share.GameClient;
import de.kryptondev.spacy.share.Version;
import java.util.ArrayList;

public class SpacyServer implements Disposable{
    private static SpacyServer instance;
    private int maxSlots = 32;
    private Server server;
    private Listener listener;
    public static final Version serverVersion = new Version(1,0,0);
    private ArrayList<GameClient> clients = new ArrayList<GameClient>(maxSlots);
    
    public void WriteWarning(String s){
        System.out.println(s);
    }
    public void WriteInfo(String s){
        System.out.println(s);
    }
    public void WriteError(String s){
        System.err.println(s);
    }
    
    public SpacyServer() {
        if(instance == null)
            instance = this;
        server = new Server();
    }
    
     
    
    public boolean start() {
        try{
        server.start();
        server.bind(30300);
        listener = new Listener() {
            @Override
            public void connected(Connection cnctn) {
                GameClient gc = new GameClient(cnctn);
                clients.add(gc);
                super.connected(cnctn);
            }      
        };
               
        
        server.addListener(listener);
        }
        catch (Exception ex) {
            WriteError(ex.getMessage());
            WriteError("Maybe a server is already running on this port?");
            return false;
        }
        return true;
    }
    
    public void addClient(GameClient gc){
        this.clients.add(gc);
    }
    
    public void removeClient(GameClient gc){
        this.clients.remove(gc);
    }
    
    @Override
    public void dispose() {
       server.close();
    }

    public int getMaxSlots() {
        return maxSlots;
    }

    public Server getServer() {
        return server;
    }

    public Listener getListener() {
        return listener;
    }

    public ArrayList<GameClient> getClients() {
        return clients;
    }

    public static SpacyServer getInstance() {
        return instance;
    }

    public static void setInstance(SpacyServer instance) {
        SpacyServer.instance = instance;
    }
    
    
}
