package mlos.amw.ex1_1;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.Opcodes;

public class VerboseCalls extends ClassVisitor {

    public VerboseCalls(ClassVisitor sink) {
        super(Opcodes.ASM5, sink);
    }

}
