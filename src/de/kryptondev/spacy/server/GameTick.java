package de.kryptondev.spacy.server;

import com.esotericsoftware.kryonet.Connection;
import de.kryptondev.spacy.data.DebugTickDelta;
import de.kryptondev.spacy.data.EMoving;
import de.kryptondev.spacy.data.Projectile;
import de.kryptondev.spacy.data.Ship;
import de.kryptondev.spacy.server.GameClient.SGameClient;
import de.kryptondev.spacy.share.DeleteEntity;
import de.kryptondev.spacy.share.playerEvents.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;


public class GameTick implements Runnable{
    private int delta = 0;
    private GameTick instance;
    public static final int ticksPerSecond = 100;        
    private SpacyServer server;    
    public GameTick(SpacyServer server) {
        this.server = server;
    }

    private void onTick(int delta){       
        for(Connection c : (Connection[])server.getServer().getConnections().clone()) {            
            SGameClient gc = (SGameClient)c;
            Ship ship = gc.getMyShip();
            if(ship != null)
                if(ship.moving != EMoving.Stopped){                        
                    ship.move(delta);
                    System.out.println("Server: Ship " + ship.id + " is moving!" + ship.position.x);                    
                }
        }
        HashMap<Long, Projectile> projs = server.world.projectiles;
        long pos = projs.size();
        while(projs.containsKey(pos)){
            Projectile p = projs.get(pos--);
                    
            if(p.remainingLifetime <= 0){
                server.world.projectiles.remove(pos + 1);
                server.getServer().sendToAllTCP(new DeleteEntity(p.id));
                System.out.println("Deleting projectile " + p.id);
                continue;
            }
            else{
                p.remainingLifetime--;
            }
            
            p.move(delta);
            System.out.println("Server: Projectile " + p.id + " is moving!" + p.position.x);
            
            for(Connection c : (Connection[])server.getServer().getConnections().clone()) { 
                SGameClient gc = (SGameClient)c;
                Ship ship = gc.getMyShip();
                /*System.out.println("Distance: " + p.position.distance(ship.position));                
                System.out.println("Projectile @ " + p.position.x + ", " + p.position.y);
                System.out.println("Ship @ " + ship.position.x + ", " + ship.position.y);
                */
                if( ship.id != p.senderId && p.position.distance(ship.position) - ship.boundsRadius - p.boundsRadius - p.damagerange <= 0){
                    ship.hit(p);
                    server.getServer().sendToAllTCP(new OnHit(ship.id, p.senderId, p.id));
                    //Zu wenig HP + Shield?
                    if(ship.hp + (ship.shield != null ? ship.shield.life : 0) < 1){
                        gc.sendTCP(new OnDeath(p.senderId, p.id));
                        server.getServer().sendToAllTCP(new OnKill(ship.id, p.senderId, p.id));
                        //TODO Client neues Schiff zuweisen & altes Schiff löschen!
                    }
                    if(p.destroyOnCollision){
                        server.world.projectiles.remove(pos + 1);
                        server.getServer().sendToAllTCP(new DeleteEntity(p.id));
                        System.out.println("Deleting projectile " + p.id);
                        continue;
                    }
                }
            }
         }
    }

    @Override
    public void run() {
        int lastDelta = delta;
        while(true){
            try {        
                long start = new Date().getTime();
                onTick(delta);
                long end = new Date().getTime();
                int time = (int)(end - start);                
                if(time > 1000 / ticksPerSecond){
                    System.err.println("Server can't keep up! Are you running on slow device?");
                    continue;
                }
                Thread.sleep((1000 / ticksPerSecond) - time);           
                long finalEnd = new Date().getTime();
                
                delta = (int)(finalEnd - start);
                if(delta != lastDelta){
                    //System.out.println("Tickdelta is: " + delta);
                    server.getServer().sendToAllTCP(new DebugTickDelta(delta, ticksPerSecond));
                    lastDelta = delta;
                }
                
            } catch (InterruptedException ex) {
                return;
            }
        }
    }

    public GameTick getInstance() {
        return instance;
    }

    public void setInstance(GameTick instance) {
        this.instance = instance;
    }

    public SpacyServer getServer() {
        return server;
    }

    public int getDelta() {
        return delta;
    }
    
    
}
