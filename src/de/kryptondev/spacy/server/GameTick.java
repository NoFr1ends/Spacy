package de.kryptondev.spacy.server;

import com.esotericsoftware.kryonet.Connection;
import de.kryptondev.spacy.data.Projectile;
import de.kryptondev.spacy.data.Ship;
import de.kryptondev.spacy.server.GameClient.SGameClient;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.newdawn.slick.geom.Vector2f;


public class GameTick implements Runnable{
    private GameTick instance;
    public static final int ticksPerSecond = 16;        
    private SpacyServer server;    
    public GameTick(SpacyServer server) {
        this.server = server;
    }

    private void onTick(){       
        //TODO: Check for collisions
        
        
        
        for(Connection c : server.getServer().getConnections()) {            
            SGameClient gc = (SGameClient)c;
            Ship ship = gc.getMyShip();
            if(ship.isMoving){
                ship.move();
            }            
        }
        
        
        List<Projectile> projectiles = server.world.projectiles;
        Iterator<Projectile> i= projectiles.iterator();
        while (i.hasNext()) {
            Projectile p = i.next();
            if(p.remainingLifetime <= 0){
                i.remove();
                System.out.println("Deleting projectile " + p.id);
                continue;
            }
            else{
                p.remainingLifetime--;
            }

            if(p.isMoving){
              p.move();
            }
         }
      
        //TODO Move to GameMasterTick
        server.getServer().sendToAllTCP(this.server.world);
    }

    @Override
    public void run() {
        while(true){
            try {               
                onTick();
                Thread.sleep(1000 / ticksPerSecond);             
          
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
    
    
}
