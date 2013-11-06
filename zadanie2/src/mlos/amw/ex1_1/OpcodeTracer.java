package mlos.amw.ex1_1;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public final class OpcodeTracer extends ClassVisitor {

    private String className;
    
    public OpcodeTracer(ClassVisitor sink) {
        super(Opcodes.ASM5, sink);
    }

    @Override
    public void visit(int version, int access, String name, String signature,
            String superName, String[] interfaces) {
        this.className = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int acc, String name, String desc,
            String sig, String[] ex) {

        MethodVisitor mv = super.visitMethod(acc, name, desc, sig, ex);
        boolean skip = name.equals("<init>") || name.equals("<clinit>");
        return skip ? mv : new OpcodeTracerMV(mv, className, acc, name, desc);
    }

}