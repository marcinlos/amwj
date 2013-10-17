package mlos.amw.ex1_1;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class VerboseCalls extends ClassVisitor {

    public VerboseCalls(ClassVisitor sink) {
        super(Opcodes.ASM5, sink);
    }
    
    @Override
    public MethodVisitor visitMethod(int access, String name, String desc,
            String signature, String[] exceptions) {
        MethodVisitor mv = cv.visitMethod(access, name, desc, signature, exceptions);
        return new PrintMethodVisitor(mv);
    }


}
