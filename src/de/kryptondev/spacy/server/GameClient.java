package de.kryptondev.spacy.server;

import com.esotericsoftware.kryonet.*;
import de.kryptondev.spacy.data.DebugTickDelta;
import de.kryptondev.spacy.data.EMoving;
import de.kryptondev.spacy.data.Projectile;
import de.kryptondev.spacy.data.Ship;
import de.kryptondev.spacy.share.Chatmessage;
import de.kryptondev.spacy.share.ConnectionAttemptResponse;
import de.kryptondev.spacy.share.PlayerInfo;
import de.kryptondev.spacy.share.Move;
import de.kryptondev.spacy.share.PlayerRotate;
import de.kryptondev.spacy.share.Version;
import de.kryptondev.spacy.share.playerEvents.OnJoin;

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
        private SpacyServer server;
        private GameClient gameClient;
        private long shipId;
        public SGameClient(SpacyServer server, GameClient gc) {
            this.gameClient = gc;
            this.server = server;
            this.connectionTimeStamp = new Date();     
            
        }
        
        
        
        
        /**
        * Neues Schiff mit Standartwerten erstellen und zur Welt hinzufügen.
        * @return das neue Schiff
        */
        public Ship addShip(){
            Ship s = new Ship();
            Random r = new Random();
            s.position = /*new Vector2f(r.nextFloat() * this.getSpacyServer().world.worldSize,r.nextFloat() * this.getSpacyServer().world.worldSize);*/ 
                    new Vector2f(r.nextFloat() * 200, r.nextFloat() * 200);
            s.moving = EMoving.Stopped;
            s.maxSpeed = 80f;
            s.acceleration = 1.2f;
            s.texture = "playerShip2_orange.png"; // todo change for teams etc
            s.boundsRadius = 87f;
            s.textureBounds = new Vector2f(99f, 75f);
            s.id = server.EntityCounter++;
            this.shipId = s.id;
            server.world.ships.put(s.id,s);
            server.getServer().sendToAllTCP(new OnJoin(s));
            
            return s;
        }
        
        private void validateConnection() {       
            if (server.isPlayerBanned(playerInfo.playerUID)) {
                this.sendTCP(new ConnectionAttemptResponse(ConnectionAttemptResponse.Type.Banned, 0));
                return;
            }
            //GameClient.this.spacyServer.broadcast(new Chatmessage(GameClient.this.getPlayerInfo().playerName + " joined the party!"));
            //GameClient.this.instance.getSpacyServer().writeInfo(GameClient.this.toString() + " connected right now!");
          
            Ship s = this.addShip();
            this.sendTCP(s);            
            this.sendTCP(new DebugTickDelta(server.getServerTick().getDelta(), GameTick.ticksPerSecond));
            server.sendWorld(this);
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
            /*GameClient.this.instance.getSpacyServer().writeInfo(GameClient.this.instance.playerInfo.playerName + " left the party!");      
            System.out.println(".disconnected()");
            */
        }
        
        public void onRecv(Object data) {        
            if (data instanceof Version) {
                this.version = (Version)data;
                if(versionMismatch(this.version)){
                    this.sendTCP(new ConnectionAttemptResponse(ConnectionAttemptResponse.Type.VersionMismatch, 0));
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
                Ship ship = this.getMyShip();
                ship.moving = move.status;
                if(this.server.world.ships.containsKey(this.shipId))
                    this.server.world.ships.replace(shipId, ship);
                
                move.id = this.shipId;
                server.getServer().sendToAllTCP(move);
                return;
            }
            if(data instanceof PlayerRotate){
                PlayerRotate rotation = (PlayerRotate)data;
                rotation.ship = this.getMyShip().id;
                this.server.getServer().sendToAllTCP(rotation);
                return;
            }
            if(data instanceof Projectile){               
                //Fire
                Projectile p = (Projectile)data;
                p.setLifeTime(2.5f);                
                p.maxSpeed = 50;
                p.speed = p.maxSpeed;
                p.visible = true;
                p.position = this.getMyShip().position;
                p.direction = this.getMyShip().direction;
                p.id = SpacyServer.instance.EntityCounter++;
                p.boundsRadius = 4.5f*3;
                p.damage = 30;
                p.moving = EMoving.FullSpeed;
                p.texture ="laserRed01.png";
                SpacyServer.instance.world.projectiles.put(p.id, p);
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
            return server;
        }

        public boolean isAdmin() {
            return server.isPlayerAdmin(this.playerInfo.playerUID);
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
            return server.world.ships.getOrDefault(this.shipId, null);
        }
            
        
    }
}
