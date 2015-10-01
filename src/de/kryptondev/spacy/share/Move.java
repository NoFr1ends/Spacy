package de.kryptondev.spacy.share;

import de.kryptondev.spacy.data.EMoving;
import org.newdawn.slick.geom.Vector2f;


public class Move {  
    public EMoving status;
    public long id;
    public Vector2f pos;
    
    public Move() {
    }

    public Move(EMoving status, long id, Vector2f pos) {
        this.status = status;
        this.id = id;
        this.pos = pos;
    }

  
}
