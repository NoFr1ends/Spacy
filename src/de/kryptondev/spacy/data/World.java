package de.kryptondev.spacy.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class World {
    
    public volatile HashMap<Long, Ship> ships;
    public volatile HashMap<Long, Projectile> projectiles;
    public volatile HashMap<Long, Entity> entities;  //alles was nicht in einer der anderen Listen steht
    //Worldsize in Pixeln
    public int worldSize = 4096 * 2;
            
    
    public ArrayList<Entity> getAllEntities(){
        ArrayList<Entity> all = new ArrayList<>(ships.size() + projectiles.size() + entities.size());
        all.addAll((ArrayList<Entity>)ships.clone());
        all.addAll((ArrayList<Entity>)projectiles.clone());        
        all.addAll((ArrayList<Entity>)entities.clone());
        return all;        
    }
    
    public World() {
        ships = new HashMap<>(2);
        projectiles = new HashMap<>(64);
        entities = new HashMap<>(0);
    }
    
}
