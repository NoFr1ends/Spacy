package de.kryptondev.spacy.share;


public class ConnectionAttempt {

    public ConnectionAttempt(Type type) {
        this.type = type;
    }
    
    Type type;
    enum Type{
        OK,
        VersionMismatch,
        Banned
    }    
}
