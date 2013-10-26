import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

import mlos.amw.ex1_1.LogFieldAccess;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class Transform {
    
    public static final class Visitor extends ClassVisitor {

        public Visitor(ClassVisitor sink) {
            super(Opcodes.ASM5, sink);
        }
        
        @Override
        public MethodVisitor visitMethod(int acc, String name, String desc,
                String sig, String[] ex) {
            MethodVisitor mv = cv.visitMethod(acc, name, desc, sig, ex);
            return new LogFieldAccess(mv, acc, name, desc);
        }
        
    }
    
    private static void run(String input, String output) throws IOException {
        byte[] original = Files.readAllBytes(Paths.get(input));
        
        ClassReader reader = new ClassReader(original);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        
        ClassVisitor transformer = new Visitor(writer);
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
