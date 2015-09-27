package de.kryptondev.spacy.share;

import de.kryptondev.spacy.data.EMoving;


public class Move {  
    public EMoving status;
    public long id;
    public Move() {
    }

    public Move(EMoving status, long id) {
        this.status = status;
        this.id = id;
    }

  
}
