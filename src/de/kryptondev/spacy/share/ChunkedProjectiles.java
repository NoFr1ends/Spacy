package de.kryptondev.spacy.share;

import de.kryptondev.spacy.data.Projectile;
import java.util.HashMap;

public class ChunkedProjectiles {
    public HashMap<Long, Projectile> projectiles;
    public ChunkedProjectiles() {
    }

    public ChunkedProjectiles(HashMap<Long, Projectile> projectiles) {
        this.projectiles = projectiles;
    }
    
    
}
