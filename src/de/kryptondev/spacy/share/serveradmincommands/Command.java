package de.kryptondev.spacy.share.serveradmincommands;

import de.kryptondev.spacy.server.GameClient;
import de.kryptondev.spacy.server.SpacyServer;
import java.util.logging.Logger;

public abstract class Command {

    private final String command;
    private String help = "Help is currently not avalible";    
    boolean adminCommand = true;
    public String params;
    
    public Command(String command, String help) {
        this.command = command;
        this.help = help;        

    }

    public Command(String command) {
        this.command = command;
       
    }
    
    /**
     * Gets called when the player enters the command in console/chat.
     *
     * @param params The parameters (without the command itself)
     */
    public abstract void onAction(GameClient sender);
   
    public String getCommand() {
        return command;
    }

    public String getHelp() {
        return help;
    }

    public boolean isAdminCommand() {
        return adminCommand;
    }

    public void setAdminCommand(boolean adminCommand) {
        this.adminCommand = adminCommand;
    }

    
}
