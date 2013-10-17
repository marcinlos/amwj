package mlos.amw.ex1_1;

import static org.objectweb.asm.Opcodes.INVOKESPECIAL;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.commons.InstructionAdapter;

public class PrintMethodVisitor extends InstructionAdapter {
    
    private final Printer printer;
    
    public PrintMethodVisitor(MethodVisitor mv) {
        super(mv);
        this.printer = new Printer(mv);
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name, String desc) {
        String fullName = name + desc;
        
        printer.syso("About to call " + fullName + "\n");
        
        // call the method
        mv.visitMethodInsn(opcode, owner, name, desc);
        
        if (opcode != INVOKESPECIAL) {
            printer.syso("Got result ");
            String returnedType = TypeHelper.getMethodType(desc);
            printer.printTopOfStack(returnedType);
            printer.syso("\n");
        }
    }

}