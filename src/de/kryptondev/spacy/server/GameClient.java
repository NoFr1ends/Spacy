package de.kryptondev.spacy.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.kryptondev.spacy.server.SpacyServer;
import de.kryptondev.spacy.share.ConnectionAttemptResponse;
import de.kryptondev.spacy.share.PlayerInfo;
import de.kryptondev.spacy.share.Version;

import java.time.Instant;
import java.util.Date;
import sun.security.jca.GetInstance;

public class GameClient {

    public Connection connection;
    public Version version;
    public Date connectionTimeStamp;
    public PlayerInfo playerInfo;
    
    public GameClient(Connection connection) {
        
        this.connection = connection;
        this.connectionTimeStamp = new Date(Instant.now().getEpochSecond());
        this.connection.addListener(new Listener() {
            @Override
            public void disconnected(Connection cnctn) {
                onDisconnect(cnctn);
                super.disconnected(cnctn);
            }
            @Override
            public void connected(Connection cnctn) {
            
            }
            
        });
    }
    private void onDisconnect(Connection cnctn){
        SpacyServer.getInstance().removeClient(this);
    }
    
    
    private void validateConnection(Object data){
        if(versionMismatch(data)){
            connection.sendTCP(new ConnectionAttemptResponse(ConnectionAttemptResponse.Type.VersionMismatch));
            return;
        }
        if(isClientBanned()){
            connection.sendTCP(new ConnectionAttemptResponse(ConnectionAttemptResponse.Type.Banned));
            return;
        }
        //Receive PlayerInfo Object        
    }

    @Override
    public String toString() {
        return "Player '" +playerInfo.playerName +  "' with '" + 
                connection.getRemoteAddressTCP().getHostString() + "' on " + 
                playerInfo.OS + ". Spacy Client "  + this.version.toString() + " is connected since " + connectionTimeStamp + "(" + (Instant.now().getEpochSecond() - connectionTimeStamp.getTime()) + ")";
    }
    
    
    private boolean isClientBanned(){
        //TODO: Implement
        return false;
    }
    
    private boolean versionMismatch(Version version){
        return version.isCompatible(SpacyServer.serverVersion);
    }
    
    public void onRecv(Object data){
        //todo: improwve performance
        if(data instanceof Version)
            validateConnection(data);
        if(data instanceof PlayerInfo)
            playerInfo = (PlayerInfo)data;
    }
}
