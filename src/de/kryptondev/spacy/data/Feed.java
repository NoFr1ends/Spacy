package de.kryptondev.spacy.data;

import java.util.Date;


public class Feed {
    private String message;
    private Date spawnTime = new Date();

    public Feed(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public Date getSpawnTime() {
        return spawnTime;
    }
    
    
}
