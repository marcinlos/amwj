package mlos.amw.ex4;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.tree.ClassNode;

public class InlinerMV extends GeneratorAdapter {

    private final ClassNode inlined;

    public InlinerMV(ClassNode inlined, MethodVisitor mv, int acc, String name,
            String desc) {
        super(mv, acc, name, desc);
        this.inlined = inlined;
    }
    
    private boolean isInit(String name) {
        return name.contains("<");
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name,
            String desc) {
        if (!isInit(name) && owner.equals(inlined.name)) {
            System.out.printf("Inlining %s/%s:%s\n", owner, name, desc);
            super.visitMethodInsn(opcode, owner, name, desc);
        } else {
            super.visitMethodInsn(opcode, owner, name, desc);
        }
    }

}