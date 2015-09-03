package de.kryptondev.spacy.share.serveradmincommands;

import de.kryptondev.spacy.server.GameClient;


public class Ban extends Command{

    public Ban(GameClient gc) {
        super("ban", gc);
    }

    @Override
    public void onAction(String params) {
        
    }
    
}
