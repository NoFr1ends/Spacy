package de.kryptondev.spacy.share.playerEvents;

/**
 * Wird an alle Clients gesendet, wenn ein Projektil ein Schiff getroffen hat.
 */
public class OnHit {
    public long victim;
    public long killer;
    public long projectile;

    public OnHit() {
    }

    public OnHit(long victim, long killer, long projectile) {
        this.victim = victim;
        this.killer = killer;
        this.projectile = projectile;
    }
    
}
