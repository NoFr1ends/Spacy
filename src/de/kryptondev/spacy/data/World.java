package de.kryptondev.spacy.data;

import java.util.ArrayList;

public class World {
    
    public ArrayList<Ship> ships;
    public ArrayList<Projectile> projectiles;
    public ArrayList<Entity> entities;//alles was nicht in einer der anderen Listen steht

    public World() {
        ships = new ArrayList<>(1);
        projectiles = new ArrayList<>(256);
        entities = new ArrayList<>();
    }
    
}
