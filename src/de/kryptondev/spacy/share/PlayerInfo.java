package de.kryptondev.spacy.share;

public class PlayerInfo extends NetworkPackage {
    public String OS;
    public String playerName;   
    public byte[] playerUID;

    public PlayerInfo() {
    }

    public PlayerInfo(String OS, String playerName, byte[] playerUID) {
        this.OS = OS;
        this.playerName = playerName;
        this.playerUID = playerUID;
    }
    
}
