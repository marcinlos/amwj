import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import mlos.amw.ex1_1.LogMethodCalls;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

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
        ClassReader reader = new ClassReader(new FileInputStream(input));
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);
        ClassVisitor transformer = new Visitor(writer);
        reader.accept(transformer, ClassReader.EXPAND_FRAMES);
        
        OutputStream ostream = new FileOutputStream(output);
        ostream.write(writer.toByteArray());
        ostream.close();
    }

    public static void main(String[] args) throws IOException {
        String path = new File("classes", args[0]).getPath();
        String input = path;
        String output = path;
        run(input, output);
    }

}
