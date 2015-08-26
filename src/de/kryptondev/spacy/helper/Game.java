package de.kryptondev.spacy.helper;

import de.kryptondev.spacy.render.RenderState;
import org.lwjgl.LWJGLException;
import org.lwjgl.Sys;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.DisplayMode;
import org.lwjgl.opengl.GL11;

public abstract class Game {

    /**
     * Starts the main loop and initialize a OpenGL window.
     */
    public void run() {
        // Add natives folder to java.library.path for LWJGL
        try {
            Java.addLibraryPath("natives/" + Java.getOperatingSystem());
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        try {
            Display.setDisplayMode(new DisplayMode(800, 600));
            Display.create();
        } catch (LWJGLException e) {
            e.printStackTrace();

            return;
        }

        System.out.println("Using LWJGL Version: " + Sys.getVersion());
        System.out.println("Using OpenGL Version: " + 
                GL11.glGetString(GL11.GL_VERSION));

        initOGL();
        
        GL11.glClearColor(0.4f, 0.6f, 0.9f, 0f);
        
        init();
        
        long last = getTime();
        while(!Display.isCloseRequested()) {
            GL11.glClear(GL11.GL_COLOR_BUFFER_BIT);
            
            long now = getTime();

            // Update game
            update(now - last); // todo delta

            last = now;

            // Draw game
            draw();

            // Flush to screen (glEnd)
            RenderState.setRenderMode(0);
            
            Display.update(); // Handle window events (like close etc)
            Display.sync(60); // Limit to 60 FPS
        }

        // Clean up the display
        Display.destroy();
    }
    
    /**
     * Creates open gl projection
     */
    private void initOGL() {
        GL11.glMatrixMode(GL11.GL_PROJECTION);
        GL11.glLoadIdentity();
        GL11.glOrtho(0, 800, 600, 0, 1, -1);
        GL11.glMatrixMode(GL11.GL_MODELVIEW);
    }
    
    /**
     * Get the time in milliseconds
     * It's resolution is higher than System.currentTimeMillis()
     * 
     * @return The system time in milliseconds
     */
    public long getTime() {
        return (Sys.getTime() * 1000) / Sys.getTimerResolution();
    }

    /**
     * Init custom game data
     */
    public abstract void init();

    /**
     * Draw method get called every frame after update is called.
     * Do drawing staff only here!
     */
    public abstract void draw();

    /**
     * Update position here.
     * @param delta Time in milliseconds since last update
     */
    public abstract void update(long delta);

}
