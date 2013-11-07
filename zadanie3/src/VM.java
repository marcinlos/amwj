import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mlos.amw.npj.ast.Deref;
import mlos.amw.npj.ast.RValue;
import mlos.amw.npj.ast.Statement;
import mlos.amw.npj.ast.ValueVisitor;
import mlos.amw.npj.ast.Visitor;

public class VM implements Runnable, Visitor {

    private final int[] heap;
    private final int heapSize;
    private final int areaSize;

    private int base;
    private int allocOffset;

    private static final int HEADER_S = 0x10000000;
    private static final int HEADER_T = 0x20000000;
    private static final int FORWARDER_MASK = 0x80000000;

    private final Collector collector = new SSCCollector();

    private final Map<String, Integer> vars = new HashMap<>();
    private final Set<String> sVars = new HashSet<>();
    private final Set<String> tVars = new HashSet<>();

    private final List<Statement> program;

    public VM(int heapSize, List<Statement> program) {
        if (heapSize % 8 != 1) {
            throw new RuntimeException("Invalid heap size (size mod 8 != 1)");
        }
        log("Heap size = %d", heapSize);
        this.heapSize = heapSize;
        this.areaSize = (heapSize - 1) / 2;
        this.program = program;
        this.heap = new int[heapSize];
    }
    
    private void log(String msg, Object... args) {
        System.out.printf(">> " + msg + "\n", args);
    }

    @Override
    public void run() {
        for (Statement s : program) {
            log("======== " + s.toString());
            s.accept(this);
        }
    }

    private class ValueExtractor implements ValueVisitor {

        private int value;
        
        public ValueExtractor(RValue rvalue) {
            rvalue.accept(this);
        }

        @Override
        public void visitInt(int value) {
            this.value = value;
        }

        @Override
        public void visitString(String value) {
            this.value = allocString(value);
        }

        @Override
        public void visitNull() {
            this.value = 0;
        }

        @Override
        public void visitVar(Deref deref) {
            this.value = dereference(deref);
        }

    }

    private int alloc(int size) {
        int newOffset = allocOffset + size;
        int offset = allocOffset;
        
        log("Allocating %d, heap ptr %d -> %d", size, allocOffset, newOffset);
        allocOffset += size;
        if (allocOffset > heapSize) {
            throw new OutOfMemoryError();
        }
        return offset;
    }

    private int fieldOffset(String field) {
        switch (field) {
        case "f1":
            return 1;
        case "f2":
            return 2;
        case "data":
            return 3;
        default:
            throw new IllegalArgumentException("No such field");
        }
    }

    private int address(String name) {
        return vars.get(name);
    }

    private int resolve(Deref deref) {
        int address = address(deref.name);
        if (deref.target == null) {
            return address;
        }
        deref = deref.target;
        address += fieldOffset(deref.name);
        
        while (deref.target != null) {
            address = heap[base + address];
            deref = deref.target;
            address += fieldOffset(deref.name);
        }
        return address;
    }
    
    private int dereference(Deref deref) {
        int address = resolve(deref);
        if (deref.target == null) {
            return address;
        } else {
            return heap[base + address];
        }
    }

    private int allocString(String data) {
        int n = data.length();
        int ptr = alloc(2 + n);
        heap[base + ptr] = HEADER_S;
        heap[base + ptr + 1] = n;

        for (int i = 0; i < n; ++i) {
            heap[base + ptr + 2 + i] = data.charAt(i);
        }
        log("Allocated string '%s' at %d (len=%d)", data, ptr, n);
        return ptr;
    }

    @Override
    public void visitCollect() {
        NPJ.collect(heap, collector, null);
    }

    @Override
    public void visitHeapAnalyze() {
        NPJ.heapAnalyze(null, null);
    }

    @Override
    public void visitDeclS(String name, String value) {
        int ptr = allocString(value);
        log("Allocated varS %s at %d, val='%s'", name, ptr, value);
        vars.put(name, ptr);
        sVars.add(name);
    }

    @Override
    public void visitDeclT(String name) {
        int ptr = alloc(4);
        heap[base + ptr] = HEADER_T;
        log("Allocated varT %s at %d", name, ptr);
        vars.put(name, ptr);
        tVars.add(name);
    }

    @Override
    public void visitAssignment(Deref target, RValue value) {
        ValueExtractor v = new ValueExtractor(value);
        int ptr = resolve(target);
        int val = v.value;
        log("mem(%d) <- %d", ptr, val);
        heap[base + ptr] = val;
    }

    @Override
    public void visitPrintVar(String name) {
        int ptr = address(name);
        if (ptr != 0) {
            int n = heap[base + ptr + 1];
            int strBase = base + ptr + 2;

            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < n; ++i) {
                char c = (char) heap[strBase + i];
                sb.append(c);
            }
            String text = sb.toString();
            print(text);
        } else {
            print("NULL");
        }
    }

    @Override
    public void visitPrintLiteral(String text) {
        print(text);
    }
    
    private void print(String text) {
        NPJ.print(text);
    }

}
