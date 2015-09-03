package de.kryptondev.spacy.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import com.esotericsoftware.kryonet.Server;
import com.sun.media.jfxmediaimpl.MediaDisposer.Disposable;
import de.kryptondev.spacy.share.Chatmessage;
import de.kryptondev.spacy.share.ConnectionAttemptResponse;
import de.kryptondev.spacy.share.Stop;
import de.kryptondev.spacy.share.Version;
import de.kryptondev.spacy.share.serveradmincommands.Command;
import java.util.ArrayList;
import java.util.Base64;


public class SpacyServer implements Disposable{
    public static SpacyServer instance;
    private GameClient gameClient;
    private boolean visibleInLan = true;
    private int maxSlots = 32;
    private int port = 30300;
    private Server server;
    private Listener listener;
    public static final Version serverVersion = new Version(1, 0, 0);
    private ArrayList<GameClient> clients;
    private ArrayList<byte[]> bans;
    private ArrayList<byte[]> admins;    
    public ArrayList<Command> commands;
    
    public void writeWarning(String s) {
        System.out.println(s);
    }

    public void writeInfo(String s) {
        System.out.println(s);
    }

    public void writeError(String s) {
        System.err.println(s);
    }

    private void stdConstr() {
        clients = new ArrayList<>(maxSlots);
        server = new Server();
        gameClient = new GameClient(this);
        bans = new ArrayList<>(Management.getLocalBannedClients());        
        admins = new ArrayList<>(Management.getAdmins());
        commands = new ArrayList<>();
        instance = this;
        //TODO register all classes
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
    }
    
    public static boolean isBadName(String playerName){
       ArrayList<String> badNames = new ArrayList<>(16);
       badNames.add("*");      
       badNames.add("\\");       
       badNames.add("/");       
       badNames.add("admin");       
       badNames.add("\t");
       badNames.add("\n");
       badNames.add("'");
       badNames.add("\"");


       for(String c : badNames){
           if(playerName.contains(c))
               return false;
       }
       return true;
    }
    
    public GameClient getServerGameClient() {
        return gameClient;
    }
    public boolean start() {
        try {
            server.start();
            server.bind(port);
            listener = new Listener() {
                @Override
                public void connected(Connection cnctn) {
                    if (getUsedSlots() < getMaxSlots()) {
                        GameClient gc = new GameClient(cnctn);
                        clients.add(gc);
                    } else {
                        cnctn.sendTCP(new ConnectionAttemptResponse(ConnectionAttemptResponse.Type.ServerFull));
                        cnctn.close();
                    }
                    super.connected(cnctn);
                }
            };

            server.addListener(listener);
        } catch (Exception ex) {
            writeError(ex.getMessage());
            writeError("Maybe a server is already running on this port?");
            return false;
        }
        return true;
    }

    public void stop(){
        broadcast(new Stop());
    }
    
    public void stop(String reason){
        broadcast(new Stop(reason));
    }
    
    public void addClient(GameClient gc) {
        this.clients.add(gc);
    }

    public void removeClient(GameClient gc) {
        this.clients.remove(gc);
    }

    public void broadcast(Object obj){
        for(GameClient gc : getClients()){
            gc.getConnection().sendTCP(obj);
        }
    }
    
     public void broadcast(String message){
        for(GameClient gc : getClients()){
            gc.getConnection().sendTCP(new Chatmessage(message));
        }
    }
     
    public void addToBanList(GameClient gc){
        
    }
    
    public void addToBanList(byte[] gc){
        this.bans.add(gc);
        
        //Base64.getEncoder().encode(gc).toString()
    }
    
    
    @Override
    public void dispose() {
        if(server != null)
        {   
            broadcast("Server has been disposed!");
            server.close();
        }
        
        server = null;
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
    
    public GameClient getClientByName(String playerName){
        for(GameClient item : getClients())
            if(item.getPlayerInfo().playerName.equals(playerName))
                return item;
        return null;
    }
    
    public GameClient getClientByUID(byte[] uid){
        for(GameClient item : getClients())
            if(item.getPlayerInfo().playerUID == uid)
                return item;
        return null;
    }

    public int getUsedSlots() {
        return clients.size();
    }

    public int getPort() {
        return port;
    }

    public boolean isVisibleInLan() {
        return visibleInLan;
    }

    public void setVisibleInLan(boolean visibleInLan) {
        this.visibleInLan = visibleInLan;
    }

    public boolean isPlayerBanned(byte[] uid){
        ArrayList<byte[]> player = new ArrayList<byte[]>(1);
        player.add(uid);
        return bans.containsAll(player);
        //TODO: Implement hasGlobalBan
    }
    
    public boolean isPlayerAdmin(byte[] uid){
        ArrayList<byte[]> player = new ArrayList<byte[]>(1);
        player.add(uid);
        return admins.containsAll(player);
        //TODO: Implement hasGlobalBan
    }
    
}
