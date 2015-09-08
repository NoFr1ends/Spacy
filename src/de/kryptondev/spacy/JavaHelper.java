package de.kryptondev.spacy;

import java.lang.reflect.Field;
import java.util.Arrays;

public class JavaHelper {

    /**
     * Adds the specified path to the java library path
     *
     * @param pathToAdd the path to add
     * @throws Exception
     */
    public static void addLibraryPath(String pathToAdd) throws Exception{
        final Field usrPathsField = ClassLoader.class.getDeclaredField("usr_paths");
        usrPathsField.setAccessible(true);

        //get array of paths
        final String[] paths = (String[])usrPathsField.get(null);

        //check if the path to add is already present
        for(String path : paths) {
            if(path.equals(pathToAdd)) {
                return;
            }
        }

        //add the new path
        final String[] newPaths = Arrays.copyOf(paths, paths.length + 1);
        newPaths[newPaths.length-1] = pathToAdd;
        usrPathsField.set(null, newPaths);
    }

    public static String getOperatingSystem() {
        String osName = System.getProperty("os.name").toLowerCase();

        if(osName.startsWith("win"))
            return "windows";

        if(osName.startsWith("mac"))
            return "macosx";

        if(osName.startsWith("lin"))
            return "linux";

        if(osName.startsWith("sol"))
            return "solaris";

        return "unknown";
    }

}