package de.kryptondev.spacy.share;

public class PlayerEvent extends NetworkPackage {
    public enum Event{
        onShoot,
        onDeath,
        onKill
    }
}
