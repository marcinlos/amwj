package mlos.amw.ex1_1;

import static org.objectweb.asm.Opcodes.IINC;
import static org.objectweb.asm.Opcodes.INVOKEDYNAMIC;
import static org.objectweb.asm.Opcodes.LDC;
import static org.objectweb.asm.Opcodes.LOOKUPSWITCH;
import static org.objectweb.asm.Opcodes.MULTIANEWARRAY;
import static org.objectweb.asm.Opcodes.TABLESWITCH;

import org.objectweb.asm.Handle;
import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class OpcodeTracerMV extends Printer {
    
    private final Type clazz;
    
    public OpcodeTracerMV(Type clazz, MethodVisitor mv, int acc, 
            String name, String desc) {
        super(mv, acc, name, desc);
        this.clazz = clazz;
    }
    
    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        incrementUsage(opcode);
        super.visitFieldInsn(opcode, owner, name, desc);
    }
    
    @Override
    public void visitIincInsn(int var, int increment) {
        incrementUsage(IINC);
        super.visitIincInsn(var, increment);
    }
    
    @Override
    public void visitIntInsn(int opcode, int operand) {
        incrementUsage(opcode);
        super.visitIntInsn(opcode, operand);
    }
    
    @Override
    public void visitInsn(int opcode) {
        incrementUsage(opcode);
        super.visitInsn(opcode);
    }
    
    @Override
    public void visitInvokeDynamicInsn(String name, String desc, Handle bsm, Object... bsmArgs) {
        incrementUsage(INVOKEDYNAMIC);
        super.visitInvokeDynamicInsn(name, desc, bsm, bsmArgs);
    }
    
    @Override
    public void visitJumpInsn(int opcode, Label label) {
        incrementUsage(opcode);
        super.visitJumpInsn(opcode, label);
    }
    
    @Override
    public void visitLdcInsn(Object cst) {
        incrementUsage(LDC);
        super.visitLdcInsn(cst);
    }
    
    @Override
    public void visitLookupSwitchInsn(Label dflt, int[] keys, Label[] labels) {
        incrementUsage(LOOKUPSWITCH);
        super.visitLookupSwitchInsn(dflt, keys, labels);
    }
    
    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        incrementUsage(opcode);
        super.visitMethodInsn(opcode, owner, name, desc);
    }
    
    @Override
    public void visitMultiANewArrayInsn(String desc, int dims) {
        incrementUsage(MULTIANEWARRAY);
        super.visitMultiANewArrayInsn(desc, dims);
    }
    
    @Override
    public void visitTableSwitchInsn(int min, int max, Label dflt, Label... labels) {
        incrementUsage(TABLESWITCH);
        super.visitTableSwitchInsn(min, max, dflt, labels);
    }
    
    @Override
    public void visitTypeInsn(int opcode, String type) {
        incrementUsage(opcode);
        super.visitTypeInsn(opcode, type);
    }
    
    @Override
    public void visitVarInsn(int opcode, int var) {
        incrementUsage(opcode);
        super.visitVarInsn(opcode, var);
    }
    
    private void incrementUsage(int opcode) {
        push(opcode);
        invokeStatic(clazz, OpcodeTracer.INC_METHOD);
    }
    
}