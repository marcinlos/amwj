import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import mlos.amw.ex1_1.LogMethodCalls;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

import com.google.common.io.Files;

public class Transform {
    
    private static final class Visitor extends ClassVisitor {

        public Visitor(ClassVisitor sink) {
            super(Opcodes.ASM5, sink);
        }
        
        @Override
        public MethodVisitor visitMethod(int acc, String name, String desc,
                String sig, String[] ex) {
            MethodVisitor mv = cv.visitMethod(acc, name, desc, sig, ex);
            return new LogMethodCalls(mv, acc, name, desc);
        }

    }
    
    private static void run(String input, String output) throws IOException {
        byte[] original = Files.toByteArray(new File(input));
        
        ClassReader reader = new ClassReader(original);
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        
        ClassVisitor transformer = new Visitor(writer);
        reader.accept(transformer, 0);
        
        try (OutputStream out = new FileOutputStream(output)) {
            out.write(writer.toByteArray());
        }
    }

    public static void main(String[] args) throws IOException {
//        String path = args[0];
        String input = "classes/Test.class";
        String output = "Test.class";
        run(input, output);
    }

}
