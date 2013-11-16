import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import mlos.amw.npj.Heap;
import mlos.amw.npj.Vars;
import mlos.amw.npj.ast.Deref;
import mlos.amw.npj.ast.RValue;
import mlos.amw.npj.ast.Statement;
import mlos.amw.npj.ast.ValueVisitor;
import mlos.amw.npj.ast.Visitor;

public class VM implements Runnable, Visitor {

    private static final Map<String, Integer> OFFSETS;

    static {
        OFFSETS = new HashMap<String, Integer>();
        OFFSETS.put("f1", 1);
        OFFSETS.put("f2", 2);
        OFFSETS.put("data", 3);
    }

    private final Heap heap;
    private final HeapWalker walker;
    private final Collector collector;

    private Map<String, Integer> vars = new HashMap<String, Integer>();

    private final List<Statement> program;

    public VM(int heapSize, List<Statement> program) {
        this.heap = new Heap(heapSize);
        this.program = program;
        this.collector = new SSCCollector(heap, vars);
        this.walker = new HeapWalker(heap);
    }

    public static void log(String msg, Object... args) {
        System.err.printf("[VM]   " + msg + "\n", args);
    }

    @Override
    public void run() {
        for (Statement s : program) {
            log("======== " + s.toString());
            s.accept(this);
        }
    }

    private int alloc(int size) {
        int ptr = heap.alloc(size);
        if (ptr == 0) {
            log("OOM, running GC");
            gc();
            ptr = heap.alloc(size);
            if (ptr == 0) {
                throw new RuntimeException("Out of memory");
            }
        }
        return ptr;
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

    private int fieldOffset(String field) {
        return OFFSETS.get(field);
    }

    private int address(String name) {
        return vars.get(name);
    }

    private int address(Deref deref) {
        if (deref.target == null) {
            return -1;
        }

        int address = address(deref.name);
        deref = deref.target;
        address += fieldOffset(deref.name);

        while (deref.target != null) {
            address = heap.get(address);
            deref = deref.target;
            address += fieldOffset(deref.name);
        }
        return address;
    }

    private int dereference(Deref deref) {
        if (deref.target == null) {
            return vars.get(deref.name);
        } else {
            int address = address(deref);
            return heap.get(address);
        }
    }

    private int allocString(String data) {
        if (data != null) {
            int n = data.length();
            int ptr = alloc(2 + n);
            heap.put(ptr, Vars.TYPE_S);
            heap.put(ptr + 1, n);
            heap.writeString(ptr + 2, data);
            log("Allocated string \"%s\" at %d (len=%d)", data, ptr, n);
            return ptr;
        } else {
            return 0;
        }
    }

    @Override
    public void visitCollect() {
        gc();
    }

    private void gc() {
        NPJ.collect(heap.getHeap(), collector, null);
    }

    @Override
    public void visitHeapAnalyze() {
        Collection<Integer> roots = vars.values();
        walker.traverse(roots);
        NPJ.heapAnalyze(walker.tVals, walker.sVals);
    }

    @Override
    public void visitDeclS(String name, String value) {
        int address = allocString(value);
        log("Allocated varS %s at %d, val='%s'", name, address, value);
        vars.put(name, address);
    }

    @Override
    public void visitDeclT(String name) {
        int address = alloc(4);
        heap.put(address, Vars.TYPE_T);
        heap.memset(address + 1, 0, 3);
        log("Allocated varT %s at %d", name, address);
        vars.put(name, address);
    }

    private void store(Deref target, int value) {
        int ptr = address(target);
        if (ptr < 0) {
            String var = target.name;
            log("var(%s) <- %d", var, value);
            vars.put(var, value);
        } else {
            log("mem(%d) <- %d", ptr, value);
            heap.put(ptr, value);
        }
    }
    
    @Override
    public void visitAssignment(Deref target, RValue value) {
        // so that gc can reclaim memory in case new object is assigned to
        // a reference that holds large subgraph
        store(target, 0);
        int val = valueOf(value);
        store(target, val);
    }

    private int valueOf(RValue value) {
        ValueExtractor v = new ValueExtractor(value);
        int val = v.value;
        return val;
    }

    @Override
    public void visitPrintVar(String name) {
        int ptr = address(name);
        if (ptr != 0) {
            int n = heap.get(ptr + 1);
            String text = heap.readString(ptr + 2, n);
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
