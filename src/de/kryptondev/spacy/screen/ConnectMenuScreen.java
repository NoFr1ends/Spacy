package de.kryptondev.spacy.screen;

import com.esotericsoftware.kryonet.Client;
import de.kryptondev.spacy.SpacyClient;
import de.kryptondev.spacy.input.KeyInputManager;
import de.kryptondev.spacy.input.MouseInputManager;
import de.kryptondev.spacy.server.SpacyServer;
import java.awt.Font;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.lwjgl.util.Rectangle;
import org.lwjgl.util.vector.Vector2f;
import org.newdawn.slick.Color;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.TrueTypeFont;


public class ConnectMenuScreen  implements IScreen, KeyInputManager.KeyListener, 
        MouseInputManager.MouseListener {
    
    private HashMap<String, Rectangle> rectCache;
    private volatile List<String> menuEntries;
    private volatile boolean modifying = false;
    private TrueTypeFont font;
    private int currentEntry;
    private boolean exit;
    private Thread t;
    private SpacyServer testserver;
    private Client testclient = new Client();
    private IScreen prevScreen;
    
    public ConnectMenuScreen(IScreen prevScreen) {
        menuEntries = new ArrayList<>();
        menuEntries.add("Create Server");        
        t = new Thread(new ScanThread()); 
        this.prevScreen = prevScreen;
    }

    @Override
    public void onButtonDown(int button) {
        
    }

    @Override
    public void onButtonUp(int button) {
        
    }

    @Override
    public void onButtonPressed(int button) {
        String currentMenu = getMenuPointAtMouse();
        if(currentMenu != null) {
            onMenuPressed(currentMenu);
        }
    }
    
    class ScanThread implements Runnable {
        @Override
        public void run(){
            do{
                List<InetAddress> addrs = testclient.discoverHosts(54777, 2000);
                modifying = true;
                menuEntries.clear();
                menuEntries.add("Create Server");
                for(InetAddress addr : addrs){
                    if(addr.getHostAddress().endsWith(".1") && !addr.getHostAddress().equals("127.0.0.1") || menuEntries.contains(addr.getHostAddress()))
                        continue;

                    menuEntries.add(addr.getHostAddress());
                }
                rectCache = null;
                modifying = false;
            }
            while(!exit);
        }
    }
    
    
    @Override
    public void init(GameContainer gc) {
        Font awtFont = new Font("Arial", Font.BOLD, 20);
        font = new TrueTypeFont(awtFont, true);
        
        KeyInputManager.getInstance().registerListener(
                "Menu Down", 
                Input.KEY_DOWN, 
                this
        );
        
        KeyInputManager.getInstance().registerListener(
                "Menu Up", 
                Input.KEY_UP, 
                this
        );
        
        KeyInputManager.getInstance().registerListener(
                "Menu Enter", 
                Input.KEY_ENTER, 
                this
        );
        
        KeyInputManager.getInstance().registerListener(
                "ESC", 
                Input.KEY_ESCAPE, 
                this
        );
        
        MouseInputManager.getInstance().registerListener(
                "Left Button", 
                Input.MOUSE_LEFT_BUTTON, 
                this
        );
        
        t.start();       
    }
    
    public void onMenuPressed(String entry) {
        switch(entry) {
            case "Create Server":                
                testserver = new SpacyServer();
                testserver.start();
                //Todo: Connect to the created server
                break;
            default:
                //Connect to selected Entry
                SpacyClient.setInstance(new SpacyClient());
                SpacyClient.getInstance().connect(entry);
                ScreenManager.getInstance().changeScreen(new GameScreen(this,SpacyClient.getInstance()));
        }
    }

    @Override
    public void onKeyPressed(int key) {
        if(key == Input.KEY_DOWN) {
            currentEntry++;
            if(currentEntry == menuEntries.size()) {
                currentEntry = 0;
            }
        } else if(key == Input.KEY_UP) {
            currentEntry--;
            if(currentEntry < 0) {
                currentEntry = menuEntries.size() - 1;
            }
        } else if(key == Input.KEY_ESCAPE){
            exit = true;
            //TODO deregister
        }
        else if(key == Input.KEY_ENTER) {
            onMenuPressed(menuEntries.get(currentEntry));
        }
    }
    
    @Override
    public void update(GameContainer gc, int delta) {
        if(rectCache == null)
            fillCache(gc);
        
        String menuEntry = getMenuPointAtMouse();
        if(menuEntry != null) {
            currentEntry = menuEntries.indexOf(menuEntry);
        }
    }
    
    public void fillCache(GameContainer gc) {
        rectCache = new HashMap<>();
        
        int y = 100;
        int i = 0;
        for(String entry: menuEntries) {
            int width = font.getWidth(entry);
            int height = font.getHeight();
            
            Rectangle bounds = new Rectangle(
                    (gc.getWidth() - width) / 2, 
                    y, 
                    width, 
                    height
            );
            y += height + 10;
            
            rectCache.put(entry, bounds);
        }
    }

    @Override
    public void draw(GameContainer gc, Graphics g) {
        int y = 100;
        int i = 0;
      try{
        if(exit ||modifying || menuEntries.size() < 1)
            return;
        List<String> buffer = menuEntries;
        for (String entry : buffer) {
            Color color = Color.white;
            if(currentEntry == i) {
                color = Color.yellow;
            }

            font.drawString((gc.getWidth() - font.getWidth(entry)) / 2, 
                    y, entry, color);

            y+=font.getHeight(entry) + 10;

            i++;
        }
      }
      catch(java.util.ConcurrentModificationException e){
          e.printStackTrace();
      }
       
    }

    @Override
    public void onKeyDown(int key) {
       
    }

    @Override
    public void onKeyUp(int key) {
        
    }
    
    private String getMenuPointAtMouse() {
        Vector2f mousePos = MouseInputManager.getInstance().getPosition();
        
        for(Map.Entry<String, Rectangle> cache: rectCache.entrySet()) {
            if(cache.getValue().contains((int)mousePos.x, (int)mousePos.y)) {                
                return cache.getKey();
            }
        }
        
        return null;
    }
    
}
