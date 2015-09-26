package de.kryptondev.spacy.share;

import de.kryptondev.spacy.data.Ship;
import java.util.HashMap;


public class ChunkedShip {
    public HashMap<Long, Ship> ships;

    public ChunkedShip() {
    }

    public ChunkedShip(HashMap<Long, Ship> ships) {
        this.ships = ships;
    }
    
}
