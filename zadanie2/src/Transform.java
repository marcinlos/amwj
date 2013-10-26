import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import mlos.amw.ex1_1.OpcodeTracer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class Transform {
    
    private static void run(String input, String output) throws IOException {
        byte[] original = Files.readAllBytes(Paths.get(input));
        
        ClassReader reader = new ClassReader(original);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        
        ClassVisitor transformer = new OpcodeTracer(writer);
        reader.accept(transformer, 0);
        
        try (OutputStream out = new FileOutputStream(output)) {
            out.write(writer.toByteArray());
        }
    }

    public static void main(String[] args) throws IOException {
        String input = "classes/Test.class";
        String output = "Test.class";
        run(input, output);
    }

}
