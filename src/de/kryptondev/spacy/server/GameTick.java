package de.kryptondev.spacy.server;

import com.esotericsoftware.kryonet.Connection;
import de.kryptondev.spacy.data.Ship;
import de.kryptondev.spacy.server.GameClient.SGameClient;
import java.util.ArrayList;



public class GameTick implements Runnable{
    //TEST VALUES!
    public final int ticksPerSecond = 50;       
   
    private SpacyServer server;
    public GameTick(SpacyServer server) {
        this.server = server;
    }

    private void onMasterTick(){
     
        for(Ship ship : server.world.ships){  
            //TEST!
            ship.position.x -= 3;
            ship.position.y += 1;
            
        }

        server.getServer().sendToAllTCP(this.server.world);
    }
    
    private void onTick(){
       
    }

    @Override
    public void run() {
        while(true){
            try {               
                onTick();
                onMasterTick();
                Thread.sleep(1000 / ticksPerSecond);              
               
            } catch (InterruptedException ex) {
                return;
            }
        }
    }
}
