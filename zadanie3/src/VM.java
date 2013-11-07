import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import mlos.amw.npj.Heap;
import mlos.amw.npj.ast.Deref;
import mlos.amw.npj.ast.RValue;
import mlos.amw.npj.ast.Statement;
import mlos.amw.npj.ast.ValueVisitor;
import mlos.amw.npj.ast.Visitor;

public class VM implements Runnable, Visitor {

    private final Heap heap;

    public static final int HEADER_S = 0x10000000;
    public static final int HEADER_T = 0x20000000;
    public static final int FORWARDER_MASK = 0x80000000;

    private final Collector collector = new SSCCollector();

    private final Map<String, Integer> vars = new HashMap<>();
    private final Set<String> sVars = new HashSet<>();
    private final Set<String> tVars = new HashSet<>();

    private final List<Statement> program;

    public VM(int heapSize, List<Statement> program) {
        this.heap = new Heap(heapSize);
        this.program = program;
    }

    public static void log(String msg, Object... args) {
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

    private int address(Deref deref) {
        int address = address(deref.name);
        if (deref.target == null) {
            return address;
        }
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
        int address = address(deref);
        if (deref.target == null) {
            return address;
        } else {
            return heap.get(address);
        }
    }

    private int allocString(String data) {
        int n = data.length();
        int ptr = heap.alloc(2 + n);
        heap.put(ptr, HEADER_S);
        heap.put(ptr + 1, n);
        heap.writeString(ptr + 2, data);
        log("Allocated string \"%s\" at %d (len=%d)", data, ptr, n);
        return ptr;
    }

    @Override
    public void visitCollect() {
        NPJ.collect(heap.getHeap(), collector, null);
    }

    class HeapWalker {
        private final Set<Integer> visited = new HashSet<>();
        public final Set<String> sVals = new HashSet<>();
        public final Set<Integer> tVals = new HashSet<>();

        public void explore(int address) {
            if (address != 0 && visited.add(address)) {
                int header = heap.get(address);
                if ((header & HEADER_S) != 0) {
                    log("SVar at %d", address);
                    
                    int size = heap.get(address + 1);
                    String text = heap.readString(address + 2, size);
                    sVals.add(text);
                } else if ((header & HEADER_T) != 0) {
                    log("SVar at %d", address);
                    
                    int data = heap.get(address + 3 /* fieldOffset("data") */);
                    tVals.add(data);
                    int t1 = heap.get(address + 1 /* fieldOffset("t1") */);
                    int t2 = heap.get(address + 2 /* fieldOffset("t2") */);
                    explore(t1);
                    explore(t2);
                }
            }
        }
    }

    @Override
    public void visitHeapAnalyze() {
        HeapWalker walker = new HeapWalker();
        for (int address : vars.values()) {
            walker.explore(address);
        }
        NPJ.heapAnalyze(walker.tVals, walker.sVals);
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
        int ptr = heap.alloc(4);
        heap.put(ptr, HEADER_T);
        log("Allocated varT %s at %d", name, ptr);
        vars.put(name, ptr);
        tVars.add(name);
    }

    @Override
    public void visitAssignment(Deref target, RValue value) {
        ValueExtractor v = new ValueExtractor(value);
        int ptr = address(target);
        int val = v.value;
        log("mem(%d) <- %d", ptr, val);
        heap.put(ptr, val);
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
