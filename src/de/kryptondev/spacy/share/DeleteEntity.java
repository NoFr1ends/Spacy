package de.kryptondev.spacy.share;

public class DeleteEntity extends NetworkPackage  {

    public long vid;

    public DeleteEntity() {
    }

    public DeleteEntity(long vid) {
        this.vid = vid;
    }
    
    
    
}
