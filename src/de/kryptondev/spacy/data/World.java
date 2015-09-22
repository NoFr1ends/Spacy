package de.kryptondev.spacy.data;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class World {
    
    public volatile ArrayList<Ship> ships;
    public volatile ArrayList<Projectile> projectiles;
    public volatile ArrayList<Entity> entities;  //alles was nicht in einer der anderen Listen steht
    //Worldsize in Pixeln
    public int worldSize = 600;
            
    public World() {
        ships = new ArrayList<>(2);
        projectiles = new ArrayList<>(64);
        entities = new ArrayList<>(0);
    }
    
}
