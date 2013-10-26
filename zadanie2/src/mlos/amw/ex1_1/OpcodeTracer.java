package mlos.amw.ex1_1;

import static org.objectweb.asm.Opcodes.ACC_FINAL;
import static org.objectweb.asm.Opcodes.ACC_PRIVATE;
import static org.objectweb.asm.Opcodes.ACC_STATIC;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;
import org.objectweb.asm.Type;
import org.objectweb.asm.commons.GeneratorAdapter;
import org.objectweb.asm.commons.Method;
import org.objectweb.asm.commons.StaticInitMerger;

public final class OpcodeTracer extends StaticInitMerger {

    public static final String HISTOGRAM = "__opcode_histogram#";
    public static final Type INT_ARRAY = Type.getType("[I");
    public static final Method INC_METHOD = Method.getMethod("void __inc#(int)");
    
    private static final String PREFIX = "__static_init#";
    
    private Type visitedClass;

    public OpcodeTracer(ClassVisitor sink) {
        super(Opcodes.ASM5, PREFIX, sink);
    }

    @Override
    public void visit(int version, int access, String name, String signature,
            String superName, String[] interfaces) {
        this.visitedClass = Type.getType(name);
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int acc, String name, String desc,
            String sig, String[] ex) {
        MethodVisitor mv = super.visitMethod(acc, name, desc, sig, ex);
        if (name.equals("<init>") || name.equals("<clinit>")) {
            return mv;
        } else {
            return new OpcodeTracerMV(visitedClass, mv, acc, name, desc);
        }
    }

    @Override
    public void visitEnd() {
        createHistogram();
        initHistogram();
        createIncMethod();
        
        super.visitEnd();
    }

    private void createIncMethod() {
        GeneratorAdapter g = new GeneratorAdapter(ACC_PRIVATE | ACC_STATIC, 
                INC_METHOD, null, null, cv);
        
        g.getStatic(visitedClass, HISTOGRAM, INT_ARRAY);
        g.loadArg(0);
        g.dup2();
        g.arrayLoad(Type.INT_TYPE);
        g.push(1);
        g.math(GeneratorAdapter.ADD, Type.INT_TYPE);
        g.arrayStore(Type.INT_TYPE);
        g.returnValue();
        
        g.endMethod();
    }

    private void createHistogram() {
        int acc = ACC_PRIVATE | ACC_STATIC | ACC_FINAL;
        super.visitField(acc, HISTOGRAM, INT_ARRAY.getDescriptor(), null, null);
        
    }

    private void initHistogram() {
        Method clinit = Method.getMethod("void <clinit>()");
        GeneratorAdapter g = new GeneratorAdapter(ACC_STATIC, clinit, null, null, this);
        
        g.push(256);
        g.newArray(Type.INT_TYPE);
        g.putStatic(visitedClass, HISTOGRAM, INT_ARRAY);
        
        Type runtimeType = Type.getType(Runtime.class);
        Type threadType = Type.getType(Thread.class);
        Type summaryType = Type.getType("OpcodeSummary");

        g.invokeStatic(runtimeType, Method.getMethod("Runtime getRuntime()"));
        g.newInstance(threadType);
        g.dup();
        
        g.newInstance(summaryType);
        g.dup();
        g.getStatic(visitedClass, HISTOGRAM, INT_ARRAY);
        g.invokeConstructor(summaryType, Method.getMethod("void <init>(int[])"));
        
        g.invokeConstructor(threadType, Method.getMethod("void <init>(Runnable)"));
        
        g.invokeVirtual(runtimeType, Method.getMethod("void addShutdownHook(Thread)"));
        
        g.returnValue();
        
        g.endMethod();
    }
}