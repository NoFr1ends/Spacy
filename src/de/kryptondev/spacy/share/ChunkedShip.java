package de.kryptondev.spacy.share;

import de.kryptondev.spacy.data.Ship;
import java.util.ArrayList;


public class ChunkedShip  extends NetworkPackage {
    public ArrayList<Ship> ships;

    public ChunkedShip() {
    }

    public ChunkedShip(ArrayList<Ship> ships) {
        this.ships = ships;
    }
    
}
