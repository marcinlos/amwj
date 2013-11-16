package mlos.amw.ex4;

import static org.objectweb.asm.Opcodes.ASM5;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class MethodInliner extends MethodVisitor {

    private final int base;
    
    public MethodInliner(int base, MethodVisitor mv, int acc, String name, String desc) {
        super(ASM5, mv);
        this.base = base;
    }

    @Override
    public void visitVarInsn(int opcode, int index) {
        System.out.println("Index = " + index + ", base=" + base);
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
    
    @Override
    public void visitInsn(int opcode) {
        switch (opcode) {
        case Opcodes.IRETURN: break;
        case Opcodes.RETURN: break;
        case Opcodes.ARETURN: break;
        case Opcodes.LRETURN: break;
        case Opcodes.FRETURN: break;
        case Opcodes.DRETURN: break;
        default:
            super.visitInsn(opcode);
        }
    }

}
