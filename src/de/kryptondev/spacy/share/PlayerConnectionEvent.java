package de.kryptondev.spacy.share;


public class PlayerConnectionEvent {
    public enum Type{
        Connected,
        Disconnected
        //,Timeout
    }
    public Type type;

    public PlayerConnectionEvent() {
    }

    public PlayerConnectionEvent(Type type) {
        this.type = type;
    }
    
}
