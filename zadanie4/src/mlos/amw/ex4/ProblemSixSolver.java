package mlos.amw.ex4;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.objectweb.asm.ClassReader;
import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.tree.ClassNode;

public class ProblemSixSolver {

    private static void run(String input, String output, String inlined)
            throws IOException {

        ClassNode inlinedClass = getNode(inlined);

        ClassReader reader = new ClassReader(new FileInputStream(input));
        ClassWriter writer = new ClassWriter(ClassWriter.COMPUTE_FRAMES);

        ClassVisitor inliner = new Inliner(inlinedClass, writer);
        reader.accept(inliner, ClassReader.EXPAND_FRAMES);
        // ClassVisitor transformer = new ClassNode();// null;// new
        // Inliner(writer);
        // MethodInsnNode n = null;
        // n.a
        // reader.accept(transformer, ClassReader.EXPAND_FRAMES);

        OutputStream out = new FileOutputStream(output);
        out.write(writer.toByteArray());
        out.close();
    }

    private static ClassNode getNode(String file) throws IOException {
        ClassReader reader = new ClassReader(new FileInputStream(file));
        ClassNode node = new ClassNode();
        reader.accept(node, 0);
        return node;
    }

    public static void main(String[] args) throws IOException {
        String input = args[0] + ".class";
        String output = args[0] + ".class";;
        String inlined = args[1] + ".class";;

        run(input, output, inlined);
    }

}
