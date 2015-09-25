package de.kryptondev.spacy.share;

import de.kryptondev.spacy.data.Projectile;
import java.util.ArrayList;

public class ChunkedProjectiles  extends NetworkPackage {
    public ArrayList<Projectile> projectiles;
    public ChunkedProjectiles() {
    }

    public ChunkedProjectiles(ArrayList<Projectile> projectiles) {
        this.projectiles = projectiles;
    }
    
    
}
