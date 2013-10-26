import java.util.Arrays;

import org.objectweb.asm.util.Printer;


public class OpcodeSummary implements Runnable {
    
    private final int[] stats;
    
    private static class OpcodeStat implements Comparable<OpcodeStat> {
        public final int opcode;
        public final int count;
        
        public OpcodeStat(int opcode, int count) {
            this.opcode = opcode;
            this.count = count;
        }

        @Override
        public int compareTo(OpcodeStat o) {
            return -Integer.compare(count, o.count);
        }
    }

    public OpcodeSummary(int[] stats) {
        this.stats = stats;
    }

    @Override
    public void run() {
        int N = 256;
        OpcodeStat[] all = new OpcodeStat[N];
        for (int i = 0; i < N; ++ i) {
            OpcodeStat stat = new OpcodeStat(i, stats[i]);
            all[i] = stat;
        }
        Arrays.sort(all);
        int i = 0;
        while (i < N && all[i].count > 0) {
            OpcodeStat item = all[i++];
            String opcode = Printer.OPCODES[item.opcode];
            System.out.printf("%s    %d\n", opcode, item.count);
        }
    }

}
