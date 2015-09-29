package de.kryptondev.spacy;

import java.io.FileInputStream;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Vector2f;
import org.newdawn.slick.opengl.Texture;
import org.newdawn.slick.opengl.TextureLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SpriteSheet {
    
    private String descFile;
    private String textureFile;
    
    private Texture t;
    private Image i;
    
    private HashMap<String, Image> subTextures = new HashMap<>();
    
    public SpriteSheet(String descFile) {
        this.descFile = descFile;
        
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(descFile);
            
            doc.getDocumentElement().normalize();
            
            textureFile = doc.getDocumentElement().getAttribute("imagePath");
            
            try {
                t = TextureLoader.getTexture("PNG", new FileInputStream(textureFile));
                i = new Image(t);
            } catch (Exception e) {
                e.printStackTrace();
            }
            
            NodeList subTextures = doc.getElementsByTagName("SubTexture");
            for(int i = 0; i < subTextures.getLength(); i++) {
                Node subTexture = subTextures.item(i);
                
                Rectangle texture = new Rectangle();
                String name = subTexture.getAttributes()
                        .getNamedItem("name").getNodeValue();
                
                texture.setX(Integer.parseInt(subTexture.getAttributes()
                        .getNamedItem("x").getNodeValue()));
                texture.setY(Integer.parseInt(subTexture.getAttributes()
                        .getNamedItem("y").getNodeValue()));
                texture.setWidth(Integer.parseInt(subTexture.getAttributes()
                        .getNamedItem("width").getNodeValue()));
                texture.setHeight(Integer.parseInt(subTexture.getAttributes()
                        .getNamedItem("height").getNodeValue()));
                
                this.subTextures.put(name, this.i.getSubImage(texture.getX(), texture.getY(), texture.getWidth(), texture.getHeight()));
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load SpriteSheet " + descFile);
        }
    }
    
    public void draw(String name, float x, float y) {
        draw(name, x, y, 0, null);
    }
    
    public void draw(String name, float x, float y, float rotation, Vector2f centerPos) {
        if(!subTextures.containsKey(name)) {
            System.out.println("Failed to find texture " + name);
            return;
        }
        
        Image image = subTextures.get(name);
        if(centerPos != null)
            image.setCenterOfRotation(centerPos.x, centerPos.y);
        image.setRotation(rotation);
        image.draw(x, y);
    }

    private class SubTexture {
        
        private String name;
        private int x;
        private int y;
        private int width;
        private int height;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public int getX() {
            return x;
        }

        public void setX(int x) {
            this.x = x;
        }

        public int getY() {
            return y;
        }

        public void setY(int y) {
            this.y = y;
        }

        public int getWidth() {
            return width;
        }

        public void setWidth(int width) {
            this.width = width;
        }

        public int getHeight() {
            return height;
        }

        public void setHeight(int height) {
            this.height = height;
        }
        
        
    }
    
}