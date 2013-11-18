import java.util.Map;
import java.util.Map.Entry;

import mlos.amw.npj.Vars;
import mlos.amw.npj.Heap;

class SSCCollector implements Collector {

    private final Heap heap;
    private final Map<String, Integer> vars;

    public SSCCollector(Heap heap, Map<String, Integer> vars) {
        this.heap = heap;
        this.vars = vars;
    }

    @Override
    public void collect(int[] data, Map<Object, Object> params) {
        VM.log("Collecting garbage");

        heap.flip();

        VM.log("Exploring %d roots", vars.size());

        int ptr = 0;
        for (Entry<String, Integer> var : vars.entrySet()) {
            int address = var.getValue();
            String name = var.getKey();

            VM.log("Starting at %s (address: %d)", name, address);
            int newAddress = move(address);
            var.setValue(newAddress);
            if (ptr == 0) {
                ptr = newAddress;
            }
        }
        if (ptr != 0) {
            while (ptr < heap.high()) {
                ptr += inspect(ptr);
            }
        }
    }
    
    private int inspect(int address) {
        VM.log("   At %d", address);
        int header = heap.get(address);
        int type = Vars.type(header);
        if (type == Vars.TYPE_T) {
            int f1 = heap.get(address + 1);
            int f2 = heap.get(address + 2);
            heap.put(address + 1, move(f1));
            heap.put(address + 2, move(f2));
            return 4;
        } else {
            return 2 + heap.get(address + 1);
        }
    }

    private int move(int address) {
        if (address == 0)
            return 0;

        VM.log("   At %d", address);
        int header = heap.get(address);
        if (Vars.isProxy(header)) {
            int newAddress = Vars.proxyTarget(header);
            VM.log("   [already copied to %d]", newAddress);
            return newAddress;
        }
        int newAddress = copyVar(address, header);
        makeProxy(address, newAddress);
        VM.log("Copied to %d", newAddress);
        return newAddress;
    }
    
    private int copyVar(int address, int header) {
        int type = Vars.type(header);
        
        switch (type) {
        case Vars.TYPE_S: return copySVar(address);
        case Vars.TYPE_T: return copyTVar(address);
        default:
            throw new RuntimeException("Unknown var type: " + type);
        }
    }
    
    private void makeProxy(int address, int newAddress) {
        heap.put(address, Vars.proxy(newAddress));
    }

    private int copySVar(int address) {
        int n = heap.get(address + 1);
        int ptr = heap.alloc(2 + n);
        heap.memcpy(ptr, address, n + 2);
        return ptr;
    }

    private int copyTVar(int address) {
        int newAddress = heap.alloc(4);
        heap.memcpy(newAddress, address, 4);
        return newAddress;
    }

}
