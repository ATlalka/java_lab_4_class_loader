package Main;

import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class MyClassLoader  extends ClassLoader{
    private Path path;

    public MyClassLoader(Path path){
        if(!Files.isDirectory(path)) throw new IllegalArgumentException("Path must be a directory!");
        this.path = path;
    }

    public Class<?> findClass (String fileName) throws ClassNotFoundException{
        Path classFile = Paths.get(path + FileSystems.getDefault().getSeparator() + fileName.replace(".", FileSystems.getDefault().getSeparator()) + ".class");

        byte [] buf = null;
        try{
            buf = Files.readAllBytes(classFile);
        } catch (IOException e) {
            throw new ClassNotFoundException("Error in defining "+ fileName+ " in "+path, e);
        }
        
        return defineClass(fileName, buf, 0, buf.length);
    }
}
