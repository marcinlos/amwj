package mlos.amw.ex4;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.MethodNode;

public class CallInlinerMV extends GeneratorAdapter {

    private static final int MAX_FRAME_SIZE = 1024;
    
    private static final Type OBJECT_TYPE = Type.getType(Object.class);
    private static final Method GET_CLASS = Method
            .getMethod("Class getClass()");

    private final ClassNode inlined;
    private final Map<String, MethodNode> methods;
    private final Type inlinedType;
    
    private int depth = 0;

    public CallInlinerMV(ClassNode inlined, MethodVisitor mv, int acc,
            String name,
            String desc) {
        super(mv, acc, name, desc);
        this.inlined = inlined;

        methods = buildMethodMap(inlined);
        inlinedType = Type.getObjectType(inlined.name);
    }

    private Map<String, MethodNode> buildMethodMap(ClassNode inlined) {
        Map<String, MethodNode> methods = new HashMap<String, MethodNode>();

        for (MethodNode method : (List<MethodNode>) inlined.methods) {
            String fullName = method.name + method.desc;
            methods.put(fullName, method);
        }
        return methods;
    }

    private boolean isInit(String name) {
        return name.contains("<");
    }

    @Override
    public void visitMethodInsn(int opcode, String owner, String name,
            String desc) {
        if (shouldRewrite(owner, name)) {
            MethodNode method = methods.get(name + desc);

            String fullMethod = String.format("%s/%s:%s", owner, name, desc);
            System.out.printf("%s%s\n", ind(depth), fullMethod);
            Label inlinedCode = new Label();
            Label after = new Label();

            ++ depth;
            int shift = depth * MAX_FRAME_SIZE;
            LocalVarShifter shifter = new LocalVarShifter(shift, this);
            MethodInliner inliner = new MethodInliner(shifter, name, desc);
            inliner.saveArgsAsLocals();
            
            inliner.loadThis();
            invokeVirtual(OBJECT_TYPE, GET_CLASS);
            push(inlinedType);
            ifCmp(OBJECT_TYPE, EQ, inlinedCode);

            inliner.restoreArgsToStack();
            super.visitMethodInsn(opcode, owner, name, desc);
            goTo(after);
            
            visitLabel(inlinedCode);

            inliner.execute(method);
            -- depth;

            visitLabel(after);
        } else {
            super.visitMethodInsn(opcode, owner, name, desc);
        }
    }

    private String ind(int k) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < k; ++ i) {
            sb.append("   ");
        }
        String ind = sb.toString();
        return ind;
    }

    private boolean shouldRewrite(String owner, String name) {
        return !isInit(name) && owner.equals(inlined.name);
    }

}