package mlos.amw.util;

import static org.objectweb.asm.Opcodes.DUP;
import static org.objectweb.asm.Opcodes.DUP2;
import static org.objectweb.asm.Opcodes.GETSTATIC;
import static org.objectweb.asm.Opcodes.INVOKESTATIC;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.SWAP;

import org.objectweb.asm.MethodVisitor;

public class Printer {
    
    private final MethodVisitor mv;

    public Printer(MethodVisitor mv) {
        this.mv = mv;
    }
    
    private void dup(String type) {
        int opcode = TypeHelper.category(type) == 1 ? DUP : DUP2;
        mv.visitInsn(opcode);
    }
    
    public void pushSystemOut() {
        mv.visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;");
    }
    
    public void pushConst(Object c) {
        mv.visitLdcInsn(c);
    }
    
    public void printTopOfStack(String type) {
        if (! type.equals("V")) {
            dup(type);
            if (TypeHelper.isPrimitive(type)) {
                String wrName = TypeHelper.getWrapperName(type);
                String methodDesc = String.format("(%s)Ljava/lang/String;", type);
                mv.visitMethodInsn(INVOKESTATIC, wrName, "toString", methodDesc);
            }
            pushSystemOut();
            mv.visitInsn(SWAP);
            invokePrint();
        }
    }


    public void invokePrint() {
        mv.visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", 
                "(Ljava/lang/Object;)V");
    }

    public void print(String string) {
        pushSystemOut();
        pushConst(string);
        invokePrint();
    }
    
    public void println(String string) {
        print(string + "\n");
    }
    

}
