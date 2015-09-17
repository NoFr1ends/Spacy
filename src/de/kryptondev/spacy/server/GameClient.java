package de.kryptondev.spacy.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.kryptondev.spacy.data.Ship;
import de.kryptondev.spacy.server.SpacyServer;
import de.kryptondev.spacy.share.Chatmessage;
import de.kryptondev.spacy.share.ConnectionAttemptResponse;
import de.kryptondev.spacy.share.PlayerInfo;
import de.kryptondev.spacy.share.Version;
import java.io.Console;

import java.util.Date;
import org.lwjgl.util.vector.Vector2f;

public class GameClient {
    private Connection connection;
    private Version version;
    private Date connectionTimeStamp;
    private PlayerInfo playerInfo;
    private SpacyServer spacyServer;
    
    public GameClient(Connection connection, SpacyServer server) {
        this.spacyServer = server;
        this.connection = connection;
        this.connectionTimeStamp = new Date();
        this.connection.addListener(new Listener() {
            @Override
            public void disconnected(Connection cnctn) {         
                //GameClient.this.getSpacyServer().writeInfo(GameClient.this.playerInfo.playerName + " left the party!");
                //GameClient.this.getSpacyServer().broadcast(GameClient.this.playerInfo.playerName + " left the party!");
                System.out.println(".disconnected()");
                GameClient.this.spacyServer.removeClient(GameClient.this);                
                super.disconnected(cnctn);
            }

            @Override
            public void received(Connection cnctn, Object o) {                
                super.received(cnctn, o);
                onRecv(o);
                
            }
            
        });
    }
   /**
    * Neues Schiff mit Standartwerten erstellen und zur Welt hinzufügen.
    * @return das neue Schiff
    */
    public Ship addShip(){
        Ship s = new Ship();
        SpacyServer.instance.world.ships.add(s);
        return s;
    }
    private void validateConnection() {       
        if (spacyServer.isPlayerBanned(playerInfo.playerUID)) {
            connection.sendTCP(new ConnectionAttemptResponse(ConnectionAttemptResponse.Type.Banned));
            return;
        }
        GameClient.this.spacyServer.broadcast(new Chatmessage(GameClient.this.getPlayerInfo().playerName + " joined the party!"));
        GameClient.this.getSpacyServer().writeInfo(GameClient.this.toString() + " connected right now!");
        
        connection.sendTCP(spacyServer.world);
        connection.sendTCP(this.addShip());
    }

    @Override
    public String toString() {
        return "Player '" + playerInfo.playerName + "' with '"
                + connection.getRemoteAddressTCP().getHostString() + "' on "
                + playerInfo.OS + ". Spacy Client " + this.version.toString() + " is connected since " + connectionTimeStamp;
    }

    private boolean versionMismatch(Version version) {
        return !version.isCompatible(SpacyServer.serverVersion);
    }

    public void onRecv(Object data) {        
        if (data instanceof Version) {
            this.version = (Version)data;
            if(versionMismatch(this.version)){
                connection.sendTCP(new ConnectionAttemptResponse(ConnectionAttemptResponse.Type.VersionMismatch));
                connection.close();
                return;
            }
        }
        if (data instanceof PlayerInfo) {
            playerInfo = (PlayerInfo) data;
            validateConnection();
            return;
        }
        if (data instanceof Chatmessage) {
            spacyServer.broadcast(data);
            return;
        }
        

    }

    public Connection getConnection() {
        return connection;
    }

    public Version getVersion() {
        return version;
    }

    public Date getConnectionTimeStamp() {
        return connectionTimeStamp;
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public SpacyServer getSpacyServer() {
        return spacyServer;
    }

    public boolean isAdmin() {
        return spacyServer.isPlayerAdmin(this.playerInfo.playerUID);
    }

}
