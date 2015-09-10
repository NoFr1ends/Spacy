package de.kryptondev.spacy.share;


public class ConnectionAttemptResponse {

    public ConnectionAttemptResponse(Type type) {
        this.type = type;
    }
    
    public Type type;
    public enum Type{
        OK,
        VersionMismatch,
        Banned,
        ServerFull,
        BadName
    }    
}
