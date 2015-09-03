package de.kryptondev.spacy;

import com.esotericsoftware.kryonet.Client;
import com.esotericsoftware.kryonet.Connection;
import com.esotericsoftware.kryonet.Listener;
import de.kryptondev.spacy.helper.KryoRegisterer;
import de.kryptondev.spacy.helper.UID;
import de.kryptondev.spacy.share.Chatmessage;
import de.kryptondev.spacy.share.ConnectionAttemptResponse;
import de.kryptondev.spacy.share.PlayerInfo;
import de.kryptondev.spacy.share.Version;

public class SpacyClient {

    private static SpacyClient instance;
    private Client client;
    private int port = 30300;
    private int timeout = 2500;
    public static final Version clientVersion = new Version(1, 0, 0);
    private Listener listener;
    private PlayerInfo info;

    public SpacyClient() {
        this.info = new PlayerInfo();
        info.OS = System.getProperty("os.name");
        info.playerUID = UID.getUID();

        //For test pourpose
        info.playerName = System.getProperty("user.name");

    }   
   

    public void connect(String server) {
        try {
            client = new Client();
            KryoRegisterer.registerAll(client.getKryo());
           //client.setKeepAliveTCP(port);

            listener = new Listener() {

                @Override
                public void received(Connection cnctn, Object o) {
                    onRecv(cnctn, o);
                }

                @Override
                public void disconnected(Connection cnctn) {
                    onDisconnect(cnctn);
                }

                @Override
                public void connected(Connection cnctn) {
                    System.out.println("Client is connected!");
                    client.sendTCP(clientVersion);
                    client.sendTCP(SpacyClient.this.info);
                   
                   
                }
            };
            client.addListener(listener);
            client.start();
            client.connect(timeout, server, port);
            
        } catch (Exception ex) {
             System.err.println(ex.getLocalizedMessage());
            //TODO Handle error
        }
    }

    private void onDisconnect(Connection cnctn) {
        //GUI Update
    }

    private void connected() {
        //GUI Update
        System.out.println("Client is connected!");
    }

    private void connectionDropped(ConnectionAttemptResponse.Type reason) {
        //GUI Update
        System.out.println("Client is not connected! Reason: " + reason.toString());
    }

    public void sendMessage(String message) {
        Chatmessage msg = new Chatmessage(message);
        msg.sender = this.info.playerName;
        this.client.sendTCP(msg);
    }

    private void onRecv(Connection cnctn, Object o) {
        if (o instanceof ConnectionAttemptResponse) {
            //Antwort auswerten
            ConnectionAttemptResponse response = (ConnectionAttemptResponse) o;
            if (response.type == ConnectionAttemptResponse.Type.OK) {
                connected();
            } else {
                this.client.close();
                connectionDropped(response.type);
            }

        }
    }

    public static SpacyClient getInstance() {
        return instance;
    }

    public static void setInstance(SpacyClient instance) {
        SpacyClient.instance = instance;
    }

    public int getTimeout() {
        return timeout;
    }

    public void setTimeout(int timeout) {
        this.timeout = timeout;
    }
    
}
