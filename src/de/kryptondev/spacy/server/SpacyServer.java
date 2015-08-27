package de.kryptondev.spacy.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.sun.media.jfxmediaimpl.MediaDisposer.Disposable;
import de.kryptondev.spacy.share.ConnectionAttemptResponse;
import de.kryptondev.spacy.share.Version;
import java.util.ArrayList;

public class SpacyServer implements Disposable{
    private int maxSlots = 32;
    private int port = 30300;
    private Server server;
    private Listener listener;
    public static final Version serverVersion = new Version(1,0,0);
    private ArrayList<GameClient> clients;
    
    public void writeWarning(String s){
        System.out.println(s);
    }
    public void writeInfo(String s){
        System.out.println(s);
    }
    public void writeError(String s){
        System.err.println(s);
    }
    
    private void stdConstr(){
        clients = new ArrayList<>(maxSlots);
        server = new Server();
    }
    
    public SpacyServer() {
        stdConstr();
    }

    public SpacyServer(int maxSlots) {
        if(maxSlots > 0)
            this.maxSlots = maxSlots;
        if(maxSlots == -1)
            this.maxSlots = Integer.MAX_VALUE;
        stdConstr();
    }

    public SpacyServer(int maxSlots, int port) {
        if(maxSlots > 0)
            this.maxSlots = maxSlots;
        if(maxSlots == -1)
            this.maxSlots = Integer.MAX_VALUE;
        
        if(port <= Short.MAX_VALUE & port > 0)
            this.port = port;
    }
    
     
    
    public boolean start() {
        try{
        server.start();
        server.bind(port);
        listener = new Listener() {
            @Override
            public void connected(Connection cnctn) {
                if (getUsedSlots() < getMaxSlots()) {
                    GameClient gc = new GameClient(cnctn);
                    clients.add(gc);
                }
                else {
                    cnctn.sendTCP(new ConnectionAttemptResponse(ConnectionAttemptResponse.Type.ServerFull));
                    cnctn.close();
                }
                super.connected(cnctn);
            }      
        };
               
        
        server.addListener(listener);
        }
        catch (Exception ex) {
            writeError(ex.getMessage());
            writeError("Maybe a server is already running on this port?");
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
    
    public int getUsedSlots(){
        return clients.size();
    }

    public int getPort() {
        return port;
    }
    
    
}
