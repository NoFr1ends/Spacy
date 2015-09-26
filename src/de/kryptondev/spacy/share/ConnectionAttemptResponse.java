package de.kryptondev.spacy.share;

import de.kryptondev.spacy.server.SpacyServer;


public class ConnectionAttemptResponse {

    public ConnectionAttemptResponse() {
    }
    
    public ConnectionAttemptResponse(Type type) {
        this.type = type;
    }
    
    public int worldSize = SpacyServer.instance.world.worldSize;
    
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
