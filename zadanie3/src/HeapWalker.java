import static mlos.amw.npj.Vars.VISITED;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import mlos.amw.npj.Heap;
import mlos.amw.npj.Vars;


class HeapWalker {
    
    public List<String> sVals;
    public List<Integer> tVals;
    
    private final Heap heap;
    
    private int m = VISITED;
    
    public HeapWalker(Heap heap) {
        this.heap = heap;
    }
    
    public void traverse(Iterable<Integer> roots) {
        sVals = new ArrayList<String>();
        tVals = new ArrayList<Integer>();
        
        for (int address : roots) {
            explore(address);
        }
        m ^= VISITED;
    }
    
    public Collection<String> getSVals() {
        return sVals;
    }
    
    public Collection<Integer> getTVals() {
        return tVals;
    }
    
    public int addMark(int header) {
        return (header | m) & ~(m ^ VISITED);
    }
    
    public boolean markAsVisited(int address) {
        int header = heap.get(address);
        int marked = addMark(header);
        boolean visited = header == marked;
        if (! visited) {
            heap.put(address, marked);
        }
        return !visited;
    }

    public void explore(int address) {
        if (address != 0 && markAsVisited(address)) {
            int header = heap.get(address);
            if (Vars.isSVar(header)) {
                VM.log("SVar at %d", address);

                int size = heap.get(address + 1);
                String text = heap.readString(address + 2, size);
                sVals.add(text);
            } else if (Vars.isTVar(header)) {
                VM.log("SVar at %d", address);

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