package de.kryptondev.spacy.share;

import de.kryptondev.spacy.data.Entity;
import java.util.ArrayList;


public class ChunkedEntity {
    public ArrayList<Entity> entities;

    public ChunkedEntity() {
    }

    public ChunkedEntity(ArrayList<Entity> entities) {
        this.entities = entities;
    }
    
}
