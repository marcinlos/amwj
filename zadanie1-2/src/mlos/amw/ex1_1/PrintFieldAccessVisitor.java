package mlos.amw.ex1_1;

import static org.objectweb.asm.Opcodes.GETFIELD;

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
        if (opcode == GETFIELD) {
            String ownerClass = owner.replace('/', '.');
            String type = TypeHelper.prettyPrint(desc);
            
            String tab = "    ";
            printer.println("Before getfield: " + ownerClass);
            printer.println(tab + type);
            printer.println(tab + name);
            printer.print(tab);
        }
        mv.visitFieldInsn(opcode, owner, name, desc);
        if (opcode == GETFIELD) {
            printer.printTopOfStack(desc);
            printer.println("");
        }
    }

}