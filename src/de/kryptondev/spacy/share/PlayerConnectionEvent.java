package de.kryptondev.spacy.share;

/**
 * Client referenziert auf die "time" in NetworkPackage 
 */
public class PlayerConnectionEvent extends NetworkPackage  {
    public enum Type{
        Connected,
        Disconnected
        //,Timeout
        //,Join
    }
    public Type type;

    public PlayerConnectionEvent() {
    }

    public PlayerConnectionEvent(Type type) {
        this.type = type;
    }
    
}
