package de.kryptondev.spacy.data;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class World {
    
    public volatile ArrayList<Ship> ships;
    public volatile ArrayList<Projectile> projectiles;
    public volatile ArrayList<Entity> entities;  //alles was nicht in einer der anderen Listen steht
    //Worldsize in Pixeln
    public int worldSize = 600;
            
    
    public ArrayList<Entity> getAllEntities(){
        ArrayList<Entity> all = new ArrayList<>(ships.size() + projectiles.size() + entities.size());
        all.addAll((ArrayList<Entity>)ships.clone());
        all.addAll((ArrayList<Entity>)projectiles.clone());        
        all.addAll((ArrayList<Entity>)entities.clone());
        return all;        
    }
    
    public World() {
        ships = new ArrayList<>(2);
        projectiles = new ArrayList<>(64);
        entities = new ArrayList<>(0);
    }
    
}
