package mlos.amw.ex1_1;

import static org.objectweb.asm.Opcodes.GETFIELD;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;

public class LogFieldAccess extends Printer {
    
    public LogFieldAccess(MethodVisitor mv, int acc, String name, String desc) {
        super(mv, acc, name, desc);
    }
    
    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        mv.visitFieldInsn(opcode, owner, name, desc);
        
        Type type = Type.getType(desc);
        if (opcode == GETFIELD && isPrimitive(type)) {
            Type ownerType = Type.getObjectType(owner);

            print("Before getfield:\n    " + ownerType.getClassName() + "\n");
            print("    " + type.getClassName() + "\n");
            print("    " + name + "\n");
            print("    ");
            printStackTop(type);
            print("\n");
        }
    }

    private boolean isPrimitive(Type type) {
        int sort = type.getSort();
        return sort != Type.OBJECT && sort != Type.ARRAY;
    }
    
}