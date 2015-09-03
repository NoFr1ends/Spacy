package de.kryptondev.spacy.share.serveradmincommands;

import de.kryptondev.spacy.server.GameClient;
import de.kryptondev.spacy.server.SpacyServer;
import java.util.regex.Pattern;

public class Kick extends Command {

   
    public Kick(String playerName) {
        super("Kick");
    }

    @Override
    public void onAction(GameClient sender) {
        GameClient c = SpacyServer.instance.getClientByName(params);
        if (c != null) {
            c.getConnection().sendTCP(this);
            sender.getSpacyServer().broadcast(c.getPlayerInfo().playerName + " was kicked!");
            c.getConnection().close();
            //c.getSpacyServer().
            //TODO addBan(uid);
        }

    }

    
}
