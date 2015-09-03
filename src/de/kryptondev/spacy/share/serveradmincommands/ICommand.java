package de.kryptondev.spacy.share.serveradmincommands;

import de.kryptondev.spacy.server.GameClient;
import java.util.logging.Logger;


public abstract class Command {
    private final String command;
    private String help = "Help is currently not avalible";
    private final GameClient sender; 

    public Command(String command, String help, GameClient sender) {
        this.command = command;
        this.help = help;
        this.sender = sender;
    }

    public Command(String command, GameClient sender) {
        this.command = command;
        this.sender = sender;
    }
    
    
    
    
    /**
     * Gets called when the player enters the command in console/chat.
     * @param params 
     * The parameter (without the command itself)
     */
    public abstract void onAction(String params);

    public GameClient getSender() {
        return sender;
    }

    public String getCommand() {
        return command;
    }

    public String getHelp() {
        return help;
    }
    
    
}
