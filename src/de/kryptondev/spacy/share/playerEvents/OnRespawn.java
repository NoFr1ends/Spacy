package de.kryptondev.spacy.share.playerEvents;


public class OnRespawn {
    public long oldId;
    public long newId;

    public OnRespawn(long oldId, long newId) {
        this.oldId = oldId;
        this.newId = newId;
    }

    public OnRespawn() {
    }
    
}
