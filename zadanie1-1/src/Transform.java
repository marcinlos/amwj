import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import mlos.amw.ex1_1.VerboseCalls;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

import com.google.common.io.Files;

public class Transform {
    private static void run(String input, String output) throws IOException {
        byte[] original = Files.toByteArray(new File(input));
        
        ClassReader reader = new ClassReader(original);
        ClassWriter writer = new ClassWriter(0);
        
        ClassVisitor transformer = new VerboseCalls(writer);
        reader.accept(transformer, 0);
        
        try (OutputStream out = new FileOutputStream(output)) {
            out.write(writer.toByteArray());
        }
    }

    public static void main(String[] args) throws IOException {
//        String path = args[0];
        run("classes/Test.class", "out.class");
    }

}
