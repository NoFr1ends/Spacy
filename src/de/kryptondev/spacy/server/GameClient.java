package de.kryptondev.spacy.server;

import com.esotericsoftware.kryonet.*;
import de.kryptondev.spacy.data.Ship;
import de.kryptondev.spacy.share.Chatmessage;
import de.kryptondev.spacy.share.ConnectionAttemptResponse;
import de.kryptondev.spacy.share.PlayerInfo;
import de.kryptondev.spacy.share.Version;

import java.util.Date;


public class GameClient extends Listener {
    public SGameClient instance;

    public GameClient(SpacyServer server) {
        instance = new SGameClient(server, this);
    }
    
    public class SGameClient extends Connection {
        private Version version;
        private final Date connectionTimeStamp;
        private PlayerInfo playerInfo;
        private SpacyServer spacyServer;
        public GameClient gameClient;

        public SGameClient(SpacyServer server, GameClient gc) {
            this.gameClient = gc;
            this.spacyServer = server;
            this.connectionTimeStamp = new Date();

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
                this.sendTCP(new ConnectionAttemptResponse(ConnectionAttemptResponse.Type.Banned));
                return;
            }
            //GameClient.this.spacyServer.broadcast(new Chatmessage(GameClient.this.getPlayerInfo().playerName + " joined the party!"));
            GameClient.this.instance.getSpacyServer().writeInfo(GameClient.this.toString() + " connected right now!");
            this.sendTCP(new ConnectionAttemptResponse(ConnectionAttemptResponse.Type.OK));

            this.sendTCP(spacyServer.world);
            this.sendTCP(this.addShip());
        }

        @Override
        public String toString() {
            return "Player '" + playerInfo.playerName + "' with '"
                    + this.getRemoteAddressTCP().getHostString() + "' on "
                    + playerInfo.OS + ". Spacy Client " + this.version.toString() + " is connected since " + connectionTimeStamp;
        }

        private boolean versionMismatch(Version version) {
            return !version.isCompatible(SpacyServer.serverVersion);
        }
        
        public void onDisconnect(){
            GameClient.this.instance.getSpacyServer().writeInfo(GameClient.this.instance.playerInfo.playerName + " left the party!");      
            System.out.println(".disconnected()");
        }
        
        public void onRecv(Object data) {        
            if (data instanceof Version) {
                this.version = (Version)data;
                if(versionMismatch(this.version)){
                    this.sendTCP(new ConnectionAttemptResponse(ConnectionAttemptResponse.Type.VersionMismatch));
                    this.close();
                    return;
                }
            }
            if (data instanceof PlayerInfo) {
                playerInfo = (PlayerInfo) data;
                validateConnection();
                return;
            }
            if (data instanceof Chatmessage) {
                //spacyServer.broadcast(data);
                return;
            }

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

        public void setPlayerInfo(PlayerInfo playerInfo) {
            this.playerInfo = playerInfo;
        }



    }
}
