package mlos.amw.ex1_1;
import java.util.Map;
import java.util.Map.Entry;
import java.util.TreeMap;

import org.objectweb.asm.util.Printer;


public class OpcodeSummary implements Runnable {
    
    private static final int[] stats = new int[256];
    
    static {
        Runtime.getRuntime().addShutdownHook(new Thread(new OpcodeSummary()));
    }
    
    @Override
    public void run() {
        Map<String, Integer> counts = new TreeMap<String, Integer>();
        for (int i = 0; i < stats.length; ++ i) {
            if (stats[i] > 4) {
                counts.put(Printer.OPCODES[i], stats[i]);
            }
        }
        for (Entry<String, Integer> e : counts.entrySet()) {
            System.out.printf("%s    %d\n", e.getKey(), e.getValue());
        }
    }
    
    public static void increment(int opcode) {
        ++ stats[opcode];
    }

}
