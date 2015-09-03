package de.kryptondev.spacy.share.serveradmincommands;

import de.kryptondev.spacy.server.GameClient;
import de.kryptondev.spacy.server.SpacyServer;

public class Ban extends Command {

    public Ban(String playerName) {
        super("Ban");
    }

    @Override
    public void onAction(GameClient sender) {
        GameClient c = SpacyServer.instance.getClientByName(params);
        if (c != null) {
            c.getConnection().sendTCP(this);
            sender.getSpacyServer().broadcast(c.getPlayerInfo().playerName + " was banned!");
            c.getConnection().close();
            //c.getSpacyServer().
            //TODO addBan(uid);
        }

    }
}
