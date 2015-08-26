package de.kryptondev.spacy.share;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.kryptondev.spacy.server.SpacyServer;
import java.time.Instant;
import java.util.Date;

public class GameClient {

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
    public Connection connection;
    public Version version;
    public Date connectionTimeStamp;
    
    private void validateConnection(Object data){
        if(versionMismatch(data)){
            connection.sendTCP(new ConnectionAttempt(ConnectionAttempt.Type.VersionMismatch));
            return;
        }
        if(isClientBanned()){
            connection.sendTCP(new ConnectionAttempt(ConnectionAttempt.Type.Banned));
            return;
        }
    }
    
    private boolean isClientBanned(){
        //TODO: Implement
        return false;
    }
    
    private boolean versionMismatch(Object version){
        Version v = (Version)version;
        return v.isCompatible(SpacyServer.serverVersion);
    }
    
    public void onRecv(Object data){
        if(data instanceof Version)
            validateConnection(data);
    }
}
