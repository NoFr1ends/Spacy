package de.kryptondev.spacy;

import de.kryptondev.spacy.render.Texture;
import java.io.FileInputStream;
import java.util.HashMap;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.TextureLoader;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class SpriteSheet {
    
    private String descFile;
    private String textureFile;
    
    private org.newdawn.slick.opengl.Texture t;
    
    private HashMap<String, SubTexture> subTextures = new HashMap<>();
    
    public SpriteSheet(String descFile) {
        this.descFile = descFile;
        
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.parse(descFile);
            
            doc.getDocumentElement().normalize();
            
            textureFile = doc.getDocumentElement().getAttribute("imagePath");
            
            NodeList subTextures = doc.getElementsByTagName("SubTexture");
            for(int i = 0; i < subTextures.getLength(); i++) {
                Node subTexture = subTextures.item(i);
                
                SubTexture texture = new SubTexture();
                texture.setName(subTexture.getAttributes()
                        .getNamedItem("name").getNodeValue());
                texture.setX(Integer.parseInt(subTexture.getAttributes()
                        .getNamedItem("x").getNodeValue()));
                texture.setY(Integer.parseInt(subTexture.getAttributes()
                        .getNamedItem("y").getNodeValue()));
                texture.setWidth(Integer.parseInt(subTexture.getAttributes()
                        .getNamedItem("width").getNodeValue()));
                texture.setHeight(Integer.parseInt(subTexture.getAttributes()
                        .getNamedItem("height").getNodeValue()));
                
                this.subTextures.put(texture.getName(), texture);
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Failed to load SpriteSheet " + descFile);
        }
        
        try {
            t = TextureLoader.getTexture("PNG", new FileInputStream(textureFile));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public Texture getTexture(String name) {
        if(!subTextures.containsKey(name))
            return null;
        
        SubTexture texture = subTextures.get(name);
        
        return new Texture(t, new Rectangle(texture.getX(), texture.getY(), 
                texture.getWidth(), texture.getHeight()));
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
