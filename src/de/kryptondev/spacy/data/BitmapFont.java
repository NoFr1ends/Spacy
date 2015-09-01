package de.kryptondev.spacy.data;

import de.kryptondev.spacy.render.Texture;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.lwjgl.util.Rectangle;
import org.newdawn.slick.opengl.TextureLoader;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

/**
 * Loading and managing a bitmap font.
 * 
 * @author jbernemann
 */
public class BitmapFont {
    
    private String name;
    private int lineHeight;
    private org.newdawn.slick.opengl.Texture texture;
    private HashMap<Character, Texture> characters;
    
    public BitmapFont(String descFile) {
        characters = new HashMap<>();
        
        loadData(descFile);
    }
    
    public Texture getCharacter(char c) {
        return characters.get(c);
    }
    
    private void loadData(String descFile) {
        // RegEx
        String infoRegEx = "info face=\"(.*)\" size=([0-9]*) bold=([01]) italic=([01]) charset=\"(.*)\" unicode=([01]) stretchH=([0-9]*) smooth=([01]) aa=([01]) padding=([0-9]*),([0-9]*),([0-9]*),([0-9]*) spacing=([0-9]*),([0-9]*) outline=([01])";
        Pattern infoPattern = Pattern.compile(infoRegEx);
        
        String commonRegEx = "common lineHeight=([0-9]*) base=([0-9]*) scaleW=([0-9]*) scaleH=([0-9]*) pages=([0-9]*) packed=([0-9]*) alphaChnl=([0-9]*) redChnl=([0-9]*) greenChnl=([0-9]*) blueChnl=([0-9]*)";
        Pattern commonPattern = Pattern.compile(commonRegEx);
        
        String pageRegEx = "page id=([0-9]*) file=\"(.*)\"";
        Pattern pagePattern = Pattern.compile(pageRegEx);
        
        String charRegEx = "char id=([0-9]*).*x=([0-9]*).*y=([0-9]*).*width=([0-9]*).*height=([0-9]*).*xoffset=([0-9]*).*yoffset=([0-9]*).*xadvance=([0-9]*).*page=([0-9]*).*chnl=([0-9]*).*";
        Pattern charPattern = Pattern.compile(charRegEx);
        
        try {
            BufferedReader reader = new BufferedReader(new FileReader(descFile));
            
            int lineNumber = 1;
            String line;
            while((line = reader.readLine()) != null) {
                if(line.startsWith("info")) {
                    Matcher matcher = infoPattern.matcher(line);
                    if(matcher.find()) {
                        name = matcher.group(1);
                         
                        // We don't need the other information here, ignore
                    } else {
                        System.out.println("Invalid info format in font '" + descFile + "'");
                    }
                } else if(line.startsWith("common")) {
                    Matcher matcher = commonPattern.matcher(line);
                    if(matcher.find()) {
                        lineHeight = Integer.parseInt(matcher.group(1));
                        
                        if(!matcher.group(5).equals("1")) 
                            throw new NotImplementedException();
                    } else {
                        System.out.println("Invalid common format in font '" + descFile + "'");
                    }
                } else if(line.startsWith("page")) {
                    Matcher matcher = pagePattern.matcher(line);
                    if(matcher.find()) {
                        texture = TextureLoader.getTexture("PNG", new FileInputStream(matcher.group(2)));
                    } else {
                        System.out.println("Invalid page format in font '" + descFile + "'");
                    }
                } else if(line.startsWith("char ")) {
                    Matcher matcher = charPattern.matcher(line);
                    if(matcher.find()) {
                        char c = 0;
                        c = (char)Integer.parseInt(matcher.group(1));
                        
                        Texture t = new Texture(texture, new Rectangle(
                                Integer.parseInt(matcher.group(2)), // x
                                Integer.parseInt(matcher.group(3)), // y
                                Integer.parseInt(matcher.group(4)), // width
                                Integer.parseInt(matcher.group(5))  // height
                        ));
                        
                        characters.put(c, t);
                    } else {
                        System.out.println("Invalid char format in font '" + descFile + "' in line " + lineNumber);
                    }
                }
                lineNumber++;
            }
        } catch (Exception e) {
            e.printStackTrace();
            
            throw new RuntimeException("Failed to load font '" + descFile + "'.", e);
        }
    }
    
}
