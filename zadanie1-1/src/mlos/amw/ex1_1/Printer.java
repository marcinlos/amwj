package mlos.amw.ex1_1;

import java.io.PrintStream;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;

public class Printer extends GeneratorAdapter {

    private static final Type printStreamType = Type.getType(PrintStream.class);

    public Printer(MethodVisitor mv, int acc, String name, String desc) {
        super(Opcodes.ASM5, mv, acc, name, desc);
    }

    public void printStackTop(Type type) {
        dup(type);
        box(type);
        getSystemOut();
        swap();
        invokePrint();
    }

    private void dup(Type type) {
        if (type.getSize() == 1) {
            dup();
        } else {
            dup2();
        }
    }

    public void print(String s) {
        getSystemOut();
        push(s);
        invokePrint();
    }

    public void invokePrint() {
        invokeVirtual(printStreamType, Method.getMethod("void print(Object)"));
    }

    public void getSystemOut() {
        getStatic(Type.getType(System.class), "out", printStreamType);
    }

}