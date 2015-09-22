package de.kryptondev.spacy.server;

import de.kryptondev.spacy.data.Ship;


public class GameTick implements Runnable{
    public final int ticksPerSecond = 16;        
    private SpacyServer server;
    public GameTick(SpacyServer server) {
        this.server = server;
    }

    private void onTick(){
        for(Ship ship: server.world.ships)         
            ship.position.x+=3;
        
        //TODO: Handle Entity.move
        //TODO: Move
        
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
}
