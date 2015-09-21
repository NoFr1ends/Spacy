package de.kryptondev.spacy.server;

import java.util.logging.Level;
import java.util.logging.Logger;


public class GameTick implements Runnable{
    public final int ticksPerSecond = 1;        
    public final int masterTicksPerSecondFactor = 2;
    private SpacyServer server;
    public GameTick(SpacyServer server) {
        this.server = server;
    }

    private void onMasterTick(){
        if(server.getServer().getConnections().length > 1){
            GameClient.SGameClient gc = (GameClient.SGameClient)server.getServer().getConnections()[0];
            gc.getMyShip().position.x+=5;
        }
        
        server.getServer().sendToAllTCP(this.server.world);
    }
    
    private void onTick(){
       
    }

    @Override
    public void run() {
        int i = 0;
        while(true){
            try {               
                onTick();
                Thread.sleep(1000 / ticksPerSecond);
                if(i >= ticksPerSecond * masterTicksPerSecondFactor){
                    i = 0;
                    onMasterTick();
                }
                i++;
            } catch (InterruptedException ex) {
                return;
            }
        }
    }
}
