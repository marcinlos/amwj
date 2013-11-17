package mlos.amw.ex4;

import java.io.PrintStream;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.MethodNode;

public class MethodInliner extends GeneratorAdapter {

    private final Type methodType;
    private final Type retType;
    private int retVar;
    private final Label afterMethod = new Label(); 
    
    public MethodInliner(MethodVisitor mv, String name, String desc) {
//        super(ASM5, mv);
        super(mv, Opcodes.ACC_PUBLIC, name, desc);
        methodType = Type.getMethodType(desc);
        retType = methodType.getReturnType();
    }
    
    public void visit(MethodNode node) {
        boolean isVoid = retType.getSort() != Type.VOID;
        
        saveLocals();

        if (isVoid) {
            retVar = newLocal(retType);
        }
        println("Body");
        node.instructions.accept(this);
        
        visitLabel(afterMethod);
        if (isVoid) {
            loadLocal(retVar);
        }
    }
    
    private void saveLocals() {
        Type[] argTypes = methodType.getArgumentTypes();
        int argc = argTypes.length;
//        int[] args = new int[argc];
//        for (int i = 0; i < argc; ++ i) {
//            args[i] = newLocal(argTypes[i]);
//        }
        for (int i = argc - 1; i >= 0; -- i) {
            storeArg(i);
//            storeLocal(args[i]);
//            visitVarInsn(argTypes[i].getOpcode(Opcodes.ISTORE), 11 + i);
        }
        visitVarInsn(Opcodes.ASTORE, 0);
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
    
    
    
    private static final Type printStreamType = Type.getType(PrintStream.class);

    public void println(String s) {
        getSystemOut();
        push(s);
        invokePrintln();
    }

    public void invokePrintln() {
        invokeVirtual(printStreamType, Method.getMethod("void println(Object)"));
    }

    public void getSystemOut() {
        getStatic(Type.getType(System.class), "out", printStreamType);
    }

}
