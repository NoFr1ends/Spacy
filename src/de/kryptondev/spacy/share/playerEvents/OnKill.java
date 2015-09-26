package de.kryptondev.spacy.share.playerEvents;

/**
 * Wird an alle Clients gesendet, wenn jemand gekillt worden ist.
 */
public class OnKill {    
    public long victim;
    public long killer;
    public long projectile;

    public OnKill() {
    }

    public OnKill(long victim, long killer, long projectile) {
        this.victim = victim;
        this.killer = killer;
        this.projectile = projectile;
    }
    
    
}
