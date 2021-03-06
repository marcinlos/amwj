package mlos.amw.ex4;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.tree.ClassNode;

public final class CallInliner extends ClassVisitor {

    private final ClassNode inlined;

    public CallInliner(ClassNode inlined, ClassVisitor sink) {
        super(Opcodes.ASM5, sink);
        this.inlined = inlined;
    }

    @Override
    public MethodVisitor visitMethod(int acc, String name, String desc,
            String sig, String[] ex) {

        MethodVisitor mv = super.visitMethod(acc, name, desc, sig, ex);
        return new CallInlinerMV(inlined, mv, acc, name, desc);
    }

}