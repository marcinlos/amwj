package mlos.amw.ex4;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.tree.MethodNode;

public class MethodInliner extends GeneratorAdapter {

    private final Type methodType;
    private final Type retType;
    private final int argc;
    private final boolean isVoid;
    private final Label afterMethod = new Label(); 

    private int retVar;
    
    public MethodInliner(MethodVisitor mv, String name, String desc) {
        super(mv, Opcodes.ACC_PUBLIC, name, desc);
        
        methodType = Type.getMethodType(desc);
        retType = methodType.getReturnType();
        argc = methodType.getArgumentTypes().length;
        isVoid = retType.getSort() == Type.VOID;
    }
    
    public void execute(MethodNode node) {
        if (!isVoid) {
            retVar = newLocal(retType);
        }
        node.instructions.accept(this);
        node.instructions.resetLabels();
        
        visitLabel(afterMethod);
        if (!isVoid) {
            loadLocal(retVar);
        }
    }
    
    public void loadThis() {
        visitVarInsn(Opcodes.ALOAD, 0);
    }
    
    public void saveArgsAsLocals() {
        for (int i = argc - 1; i >= 0; -- i) {
            storeArg(i);
        }
        visitVarInsn(Opcodes.ASTORE, 0);
    }
    
    public void restoreArgsToStack() {
        loadThis();
        for (int i = 0; i < argc; ++ i) {
            loadArg(i);
        }
    }

    @Override
    public void visitInsn(int opcode) {
        switch (opcode) {
        case Opcodes.IRETURN:
        case Opcodes.ARETURN:
        case Opcodes.LRETURN:
        case Opcodes.FRETURN:
        case Opcodes.DRETURN:
            storeLocal(retVar);
            goTo(afterMethod);
            break;
        case Opcodes.RETURN:
            goTo(afterMethod);
            break;
        default:
            super.visitInsn(opcode);
        }
    }

}
