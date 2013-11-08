import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import mlos.amw.npj.Header;
import mlos.amw.npj.Heap;

public class SSCCollector implements Collector {

    private final Heap heap;

    public SSCCollector(Heap heap) {
        this.heap = heap;
    }

    @Override
    public void collect(int[] data, Map<Object, Object> params) {
        VM.log("Collecting garbage");

        @SuppressWarnings("unchecked")
        Map<String, Integer> vars = (Map<String, Integer>) params
                .get(VM.VARS_KEY);

        heap.flip();

        VM.log("Exploring %d roots", vars.size());

        Map<String, Integer> newVars = new HashMap<>();

        for (Entry<String, Integer> var : vars.entrySet()) {
            int address = var.getValue();
            String name = var.getKey();

            VM.log("Starting at %s (address: %d)", name, address);
            int newAddress = traverse(address);
            newVars.put(name, newAddress);
        }
        params.put(VM.VARS_KEY, newVars);
    }

    private int traverse(int address) {
        if (address == 0)
            return 0;

        VM.log("   At %d", address);
        int header = heap.get(address);
        if (Header.isProxy(header)) {
            VM.log("   [already copied to %d]", heap.get(address + 1));
            return heap.get(address + 1);
        }
        heap.put(address, header | Header.PROXY);

        int newAddress = copyVar(address, header);
        heap.put(newAddress, header);
        VM.log("Copied to %d", newAddress);
        return newAddress;
    }
    
    private int copyVar(int address, int header) {
        int type = Header.type(header);
        
        switch (type) {
        case VM.TYPE_S: return copySVar(address);
        case VM.TYPE_T: return copyTVar(address);
        default:
            throw new RuntimeException("Unknown var type: " + type);
        }
    }
    
    private void makeProxy(int address, int newAddress) {
        heap.put(address + 1, newAddress);
    }

    private int copySVar(int address) {
        int n = heap.get(address + 1);
        int ptr = heap.alloc(2 + n);
        heap.memcpy(ptr + 1, address + 1, n + 1);
        
        makeProxy(address, ptr);
        return ptr;
    }

    private int copyTVar(int address) {
        int newAddress = heap.alloc(4);
        int t1 = heap.get(address + 1);
        int t2 = heap.get(address + 2);
        int data = heap.get(address + 3);
        
        makeProxy(address, newAddress);
        
        int newT1 = traverse(t1);
        int newT2 = traverse(t2);

        heap.put(newAddress + 1, newT1);
        heap.put(newAddress + 2, newT2);
        heap.put(newAddress + 3, data);

        return newAddress;
    }

}
