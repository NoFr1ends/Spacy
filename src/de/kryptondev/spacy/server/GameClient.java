package de.kryptondev.spacy.server;

import com.esotericsoftware.kryonet.*;
import de.kryptondev.spacy.data.Projectile;
import de.kryptondev.spacy.data.Rect;
import de.kryptondev.spacy.data.Ship;
import de.kryptondev.spacy.share.Chatmessage;
import de.kryptondev.spacy.share.ConnectionAttemptResponse;
import de.kryptondev.spacy.share.PlayerInfo;
import de.kryptondev.spacy.share.Move;
import de.kryptondev.spacy.share.PlayerConnectionEvent;
import de.kryptondev.spacy.share.PlayerRotate;
import de.kryptondev.spacy.share.Version;

import java.util.Date;
import java.util.Random;
import org.newdawn.slick.geom.Vector2f;



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
        private GameClient gameClient;
        private long shipId;
        
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
            Random r = new Random();
            s.position = new Vector2f(r.nextFloat() * this.getSpacyServer().world.worldSize,r.nextFloat() * this.getSpacyServer().world.worldSize);
            s.acceleration = 2f;
            s.maxSpeed = 5f;
            s.image = "playerShip1_blue.png"; // todo change for teams etc
            spacyServer.world.ships.add(s);
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
            Ship s = this.addShip();
            s.id = spacyServer.EntityCounter++;
            this.shipId = s.id;
            this.sendTCP(s);
            this.spacyServer.getServer().sendToAllTCP(new PlayerConnectionEvent(PlayerConnectionEvent.Type.Connected));
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
            if(data instanceof Move){
                Move move = (Move)data;                    
                this.getMyShip().isMoving = move.status;
                return;
            }
            if(data instanceof PlayerRotate){
                PlayerRotate rotation = (PlayerRotate)data;
                this.getMyShip().direction = rotation.direction;
                return;
            }
            if(data instanceof Projectile){               
                //Fire
                Projectile p = (Projectile)data;
                p.setLifeTime(3.0f);
                p.isMoving = true;
                p.acceleration = Float.POSITIVE_INFINITY;
                p.maxSpeed = 50;
                p.visible = true;
                p.position = this.getMyShip().position;
                p.direction = this.getMyShip().direction;
                p.id = SpacyServer.instance.EntityCounter++;
                p.bounds = new Rect(0,0,20,20);
                SpacyServer.instance.world.projectiles.add(p);
                SpacyServer.instance.getServer().sendToAllTCP(p);
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

        public GameClient getGameClient() {
            return gameClient;
        }

        public void setGameClient(GameClient gameClient) {
            this.gameClient = gameClient;
        }

        public Ship getMyShip() {
            for(Ship ship : spacyServer.world.ships){
                if(ship.id == this.shipId)
                    return ship;
            }
            return null;
        }
            
        
    }
}
