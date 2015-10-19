package de.kryptondev.spacy.data;

import de.kryptondev.spacy.share.PlayerInfo;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class World {
    
    public volatile ConcurrentHashMap<Long, Ship> ships;
    public volatile ConcurrentHashMap<Long, Projectile> projectiles;
    public volatile HashMap<Long, Entity> entities;  //alles was nicht in einer der anderen Listen steht
    public volatile HashMap<Long, PlayerInfo> players;
    //Worldsize in Pixeln
    public int worldSize = 8192;
    public int toleranceDeathRadius = 800;
    
    public World() {
        ships = new ConcurrentHashMap<>(4);
        projectiles = new ConcurrentHashMap<>(64);
        entities = new HashMap<>(0);
        players = new HashMap<>(0);
    }
    
}
