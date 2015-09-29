package de.kryptondev.spacy.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

public class World {
    
    public volatile ConcurrentHashMap<Long, Ship> ships;
    public volatile ConcurrentHashMap<Long, Projectile> projectiles;
    public volatile HashMap<Long, Entity> entities;  //alles was nicht in einer der anderen Listen steht
    //Worldsize in Pixeln
    public int worldSize = 4096 * 2;
    
    public World() {
        ships = new ConcurrentHashMap<>(4);
        projectiles = new ConcurrentHashMap<>(64);
        entities = new HashMap<>(0);
    }
    
}
