package de.kryptondev.spacy.share;

public class ConnectionAttemptResponse {

    public ConnectionAttemptResponse() {
    }
    
    public ConnectionAttemptResponse(Type type, int worldSize) {
        this.worldSize = worldSize;
        this.type = type;
    }
    
    public int worldSize;
    
    public Type type;
    public enum Type{
        OK,
        VersionMismatch,
        Banned,
        ServerFull,
        BadName,
        Disconnect
    }    
}
