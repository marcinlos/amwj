package mlos.amw.ex1_1;

import static org.objectweb.asm.Opcodes.GETFIELD;
import mlos.amw.util.Printer;
import mlos.amw.util.TypeHelper;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.InstructionAdapter;

public class PrintFieldAccessVisitor extends InstructionAdapter {
    
    private final Printer printer;
    
    public PrintFieldAccessVisitor(MethodVisitor mv) {
        super(mv);
        this.printer = new Printer(mv);
    }
    
    @Override
    public void visitFieldInsn(int opcode, String owner, String name, String desc) {
        mv.visitFieldInsn(opcode, owner, name, desc);
        
        if (opcode == GETFIELD && TypeHelper.isPrimitive(desc)) {
            String ownerClass = owner.replace('/', '.');
            String type = TypeHelper.prettyPrint(desc);
            
            printer.println("Before getfield: " + ownerClass);
            printer.println("    " + type);
            printer.println("    " + name);
            printer.print("    ");
            printer.printTopOfStack(desc);
            printer.println("");
        }
    }

}