package Resources;

import java.io.File;
import java.io.FilenameFilter;

public class Finder {


    public String[] findClasses(String path) {
        File file = new File(path);
        String[] directories = file.list((FilenameFilter) new FilenameFilter() {
            public boolean accept(File current, String name) {
                return (!new File(current, name).isDirectory() && name.contains(".class"));
            }
        });

        return directories;
    }

}
