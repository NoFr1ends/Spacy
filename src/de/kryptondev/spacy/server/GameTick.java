package de.kryptondev.spacy.server;

import de.kryptondev.spacy.data.Ship;
import org.newdawn.slick.geom.Vector2f;


public class GameTick implements Runnable{
    private GameTick instance;
    public static final int ticksPerSecond = 16;        
    private SpacyServer server;
    public GameTick(SpacyServer server) {
        this.server = server;
    }

    private void onTick(){
        
        for(Ship ship: server.world.ships) {            
            if(ship.isMoving){
                System.out.println("Moving...");
                ship.move();
            }            
        }
        
     
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
