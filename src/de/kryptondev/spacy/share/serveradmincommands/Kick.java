package de.kryptondev.spacy.share.serveradmincommands;

import de.kryptondev.spacy.server.GameClient;
import java.util.regex.Pattern;

public class Kick extends Command {

    public Kick(GameClient sender) {
        super("kick", sender);
    }

    @Override
    public void onAction(String params) {
        
    }

}