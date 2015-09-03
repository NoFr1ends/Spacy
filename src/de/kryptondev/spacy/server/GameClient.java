package de.kryptondev.spacy.server;

import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.kryptondev.spacy.server.SpacyServer;
import de.kryptondev.spacy.share.Chatmessage;
import de.kryptondev.spacy.share.ConnectionAttemptResponse;
import de.kryptondev.spacy.share.PlayerInfo;
import de.kryptondev.spacy.share.Version;
import de.kryptondev.spacy.share.serveradmincommands.BanCommand;
import de.kryptondev.spacy.share.serveradmincommands.Kick;
import de.kryptondev.spacy.share.serveradmincommands.KickCommand;

import java.time.Instant;
import java.util.Date;

public class GameClient {

    private Connection connection;
    private Version version;
    private Date connectionTimeStamp;
    private PlayerInfo playerInfo;
    private SpacyServer spacyServer;

    public GameClient(Connection connection) {

        this.connection = connection;
        this.connectionTimeStamp = new Date(Instant.now().getEpochSecond());
        this.connection.addListener(new Listener() {
            @Override
            public void disconnected(Connection cnctn) {
                onDisconnect(cnctn);
                super.disconnected(cnctn);
            }
        });
    }

    /**
     * Constructor for the internal Server-GameClient
     * @param spacyServer the Server
     */
    public GameClient(SpacyServer spacyServer) {
        this.spacyServer = spacyServer;
        this.version = SpacyServer.serverVersion;
    }
    
    
    
    private void onDisconnect(Connection cnctn){
        spacyServer.removeClient(this);
    }

    private void validateConnection(Object data) {
        if (versionMismatch((Version) data)) {
            connection.sendTCP(new ConnectionAttemptResponse(ConnectionAttemptResponse.Type.VersionMismatch));
            return;
        }
        if (spacyServer.isPlayerBanned(playerInfo.playerUID)) {
            connection.sendTCP(new ConnectionAttemptResponse(ConnectionAttemptResponse.Type.Banned));
            return;
        }
        spacyServer.broadcast(new Chatmessage(playerInfo.playerName + " joined the party!"));

    }

    @Override
    public String toString() {
        return "Player '" + playerInfo.playerName + "' with '"
                + connection.getRemoteAddressTCP().getHostString() + "' on "
                + playerInfo.OS + ". Spacy Client " + this.version.toString() + " is connected since " + connectionTimeStamp + "(" + (Instant.now().getEpochSecond() - connectionTimeStamp.getTime()) + ")";
    }

    private boolean versionMismatch(Version version) {
        return !version.isCompatible(SpacyServer.serverVersion);
    }

    public void onRecv(Object data) {
        //todo: improve performance with return statements
        if (data instanceof Version) {
            validateConnection(data);
        }
        if (data instanceof PlayerInfo) {
            playerInfo = (PlayerInfo) data;
        }
        if (data instanceof Chatmessage) {
            spacyServer.broadcast(data);
        }
        if (data instanceof KickCommand) {
            //Check wether sender is admin
            if (isAdmin()) {
                KickCommand kick = (KickCommand)data;
                for (GameClient gc : spacyServer.getClients()) {
                    if (gc.playerInfo.playerUID == kick.uid) {
                        gc.getConnection().sendTCP(new KickCommand(kick.uid));
                        gc.getConnection().close();
                        this.getSpacyServer().broadcast( gc.playerInfo.playerName + " has been kicked!");
                        return;
                    }
                }
            }
        }
        if (data instanceof BanCommand) {
            //Check wether sender is admin
            if (isAdmin()) {
                BanCommand kick = (BanCommand)data;
                for (GameClient gc : spacyServer.getClients()) {
                    if (gc.playerInfo.playerUID == kick.uid) {
                        gc.getConnection().sendTCP(new BanCommand(kick.uid));
                        gc.getConnection().close();
                        this.getSpacyServer().broadcast( gc.playerInfo.playerName + " has been banned!");
                        return;
                    }
                }
            }
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
