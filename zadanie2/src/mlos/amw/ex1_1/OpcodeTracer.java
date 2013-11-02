package mlos.amw.ex1_1;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public final class OpcodeTracer extends ClassVisitor {

    public OpcodeTracer(ClassVisitor sink) {
        super(Opcodes.ASM5, sink);
    }

    @Override
    public MethodVisitor visitMethod(int acc, String name, String desc,
            String sig, String[] ex) {
        
        MethodVisitor mv = super.visitMethod(acc, name, desc, sig, ex);
        boolean skip = name.equals("<init>") || name.equals("<clinit>");
        return skip ? mv : new OpcodeTracerMV(mv, acc, name, desc);
    }

}