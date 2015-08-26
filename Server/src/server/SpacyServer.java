package server;
import com.esotericsoftware.kryonet.*;
import com.sun.media.jfxmediaimpl.MediaDisposer.Disposable;

public class SpacyServer implements Disposable {
    Server server;
        
        
    public void WriteError(String s){
        System.err.println(s);
    }
    
    public SpacyServer() {
        server = new Server();
    }
    
    private void onRecv(Connection connection, Object object){
        
    }
    
    
    public void start() {
        try{
        server.start();
        server.bind(30300);
        server.addListener(
                new Listener() {
                    public void received (Connection connection, Object object) {
                            onRecv(connection, object);
                        }
                    });
        }
        catch (Exception ex) {
            WriteError(ex.getMessage());
            WriteError("Maybe a server is already running on this port?");
        }
    }

    @Override
    public void dispose() {
       
    }
    
    
}