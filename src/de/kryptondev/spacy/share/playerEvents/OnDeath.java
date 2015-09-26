package de.kryptondev.spacy.share.playerEvents;

/**
 * Wird gesendet wenn der Client nicht mehr genug HP hat -> gestorben ist.
 * Ausschließlich der betroffene Client empfängt dieses Paket!
 */
public class OnDeath {
    public long killer;
    public long projectile;

    public OnDeath() {
    }
        
    public OnDeath(long killer, long projectile) {
        this.killer = killer;
        this.projectile = projectile;
    }
}
