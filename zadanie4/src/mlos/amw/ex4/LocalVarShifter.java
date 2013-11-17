package mlos.amw.ex4;

import static org.objectweb.asm.Opcodes.ASM5;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class LocalVarShifter extends MethodVisitor {

    private final int base;
    
    public LocalVarShifter(int base, MethodVisitor mv) {
        super(ASM5, mv);
        this.base = base;
    }

    @Override
    public void visitVarInsn(int opcode, int index) {
        super.visitVarInsn(opcode, base + index);
    }
    
    @Override
    public void visitIntInsn(int opcode, int arg) {
        switch (opcode) {
        case Opcodes.RET: 
            super.visitIntInsn(opcode, base + arg);
            break;
        default:
            super.visitIntInsn(opcode, arg);
        }
    }
    
    @Override
    public void visitIincInsn(int var, int n) {
        super.visitIincInsn(base + var, n);
    }
    
}
