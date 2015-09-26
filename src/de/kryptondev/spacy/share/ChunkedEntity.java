package de.kryptondev.spacy.share;

import de.kryptondev.spacy.data.Entity;
import java.util.HashMap;


public class ChunkedEntity {
    public HashMap<Long, Entity> entities;

    public ChunkedEntity() {
    }

    public ChunkedEntity(HashMap<Long, Entity> entities) {
        this.entities = entities;
    }
    
}
