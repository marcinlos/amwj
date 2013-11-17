package mlos.amw.ex4;

import java.io.PrintStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.objectweb.asm.Label;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.tree.ClassNode;
import org.objectweb.asm.tree.LocalVariableNode;
import org.objectweb.asm.tree.MethodNode;
import org.objectweb.asm.tree.ParameterNode;

public class CallInlinerMV extends GeneratorAdapter {

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

        for (MethodNode method : methods(inlined)) {
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
            String fullMethod = String.format("%s/%s:%s", owner, name, desc);
            
            int k = depth;
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < k; ++ i) {
                sb.append("  ");
            }
            String ind = sb.toString();
            
            System.out.printf("%sInlining%s\n", ind, fullMethod);
//            println("Inlining " + fullMethod);
//            Label same = new Label();
//            Label after = new Label();

//            dup();
//            invokeVirtual(OBJECT_TYPE, GET_CLASS);
//            push(inlinedType);
//            ifCmp(OBJECT_TYPE, EQ, same);
            // call ordinary version
            MethodNode method = methods.get(name + desc);
//            super.visitMethodInsn(opcode, owner, name, desc);
//            goTo(after);
            
//            visitLabel(same);

            // inline
//            int thisIndex = newLocal(inlinedType);
            ++ depth;
            int n = depth * 1024;//nextLocal;
//            storeParams(desc);
//            storeLocal(thisIndex);
//            visitVarInsn(inlinedType.getOpcode(Opcodes.ISTORE), n);
            
            LocalVarShifter shifter = new LocalVarShifter(n, this);
            MethodInliner inliner = new MethodInliner(shifter, name, desc);
            inliner.visit(method);
//            -- depth;
//            println("End of " + fullMethod);
//            method.instructions.accept(inliner);
//            visitLabel(after);
        } else {
            super.visitMethodInsn(opcode, owner, name, desc);
        }
    }

    private boolean shouldRewrite(String owner, String name) {
        return !isInit(name) && owner.equals(inlined.name);
    }

    @SuppressWarnings("unchecked")
    private static List<MethodNode> methods(ClassNode node) {
        return (List<MethodNode>) node.methods;
    }

}