package de.kryptondev.spacy.share;

public class PlayerInfo {
    public long id;
    public String OS;
    public String playerName;   
    public byte[] playerUID;
    public int kills = 0;
    public int deaths = 0;

    public PlayerInfo() {
    }

    public PlayerInfo(long id, String OS, String playerName, byte[] playerUID) {
        this.id = id;
        this.OS = OS;
        this.playerName = playerName;
        this.playerUID = playerUID;
    }

}
