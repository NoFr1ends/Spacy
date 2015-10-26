package de.kryptondev.spacy.share;

import java.util.HashMap;


public class ChunkedPlayer {
    public HashMap<Long, PlayerInfo> players;

    public ChunkedPlayer() {
    }

    public ChunkedPlayer(HashMap<Long, PlayerInfo> players) {
        this.players = players;
    }    
}
