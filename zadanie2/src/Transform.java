import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import mlos.amw.ex1_1.OpcodeTracer;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;

public class Transform {
    
    private static void run(String input, String output) throws IOException {
        ClassReader reader = new ClassReader(new FileInputStream(input));
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        
        ClassVisitor transformer = new OpcodeTracer(writer);
        reader.accept(transformer, ClassReader.EXPAND_FRAMES);
        
        OutputStream out = new FileOutputStream(output);
        out.write(writer.toByteArray());
        out.close();
    }

    public static void main(String[] args) throws IOException {
        String input = "classes/Test.class";
        String output = "Test.class";
        run(input, output);
    }

}
