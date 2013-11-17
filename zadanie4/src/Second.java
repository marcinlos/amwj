import java.util.ArrayDeque;
import java.util.Deque;



public class Second {
    
    static int a;
    static {
        a = 3;
    } 
    
    public static class Range {
        public final int p;
        public final int r;
        
        public Range(int p, int r) {
           this.p = p;
           this.r = r;
        }
    }
//    
//    public int sort(int[] array) {
//        return sort(array, 0, array.length);
//    }
    
    public int sort(int[] array, int p, int r) {
        Deque<Range> parts = new ArrayDeque<Range>();
        range(p, r);
        parts.add(range(p, r));
        
        while (!parts.isEmpty()) {
            Range w = parts.poll();
            if (eligible(w)) {
                int q = partition(array, w.p, w.r);
                parts.add(range(w.p, q + 1));
                parts.add(range(q + 1, w.r));
            }
        }
        return r - p;
    }
    
    public boolean eligible(Range range) {
        return range.r - range.p > 1;
    }
    
    public Range dupa(int p, int r) {
        return new Range(p, r);
    }
    
    public Range range(int p, int r) {
        return new Range(p, r);
    }
    
    public Object dupa() {
        return System.in;
    }
    
    public Object kupa() {
        return System.out;
    }
    
    public void szczyny() {
        dupa();
//        dupa();
        System.out.println(dupa());
//        kupa();
    }
    
    public int partition(int[] array, int p, int r) {
        int i = p - 1;
        int j = r;
        int mid = array[p];
        
        while (i < j) {
            do { ++ i; } while (array[i] < mid);
            do { -- j; } while (array[j] > mid);
            if (i < j) {
                swap(array, i, j);
            } else {
                return j;
            }
        }
        return 0;
    }
    
    public void swap(int[] array, int i, int j) {
        int tmp = array[i];
        array[i] = array[j];
        array[j] = tmp;
    }
    
}
