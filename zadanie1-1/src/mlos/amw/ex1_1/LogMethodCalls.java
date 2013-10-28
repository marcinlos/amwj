package mlos.amw.ex1_1;

import static org.objectweb.asm.Opcodes.INVOKESPECIAL;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class LogMethodCalls extends Printer {
    
    public LogMethodCalls(MethodVisitor mv, int acc, String name, String desc) {
        super(mv, acc, name, desc);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        print("About to call " + name + desc + "\n");

        mv.visitMethodInsn(opcode, owner, name, desc);
        
        if (opcode != INVOKESPECIAL) {
            print("Got result ");
            Type retType = Type.getMethodType(desc).getReturnType();
            if (retType.getSort() != Type.VOID) {
                printStackTop(retType);
            }
            print("\n");
        }
    }

}