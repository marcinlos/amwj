import java.util.Random;

public class Test implements Runnable {

    private static final int MAX = 10000;
    private static final int N = 100;
    private static final int[] numbers = new int[N];
    
    static {
        genData(numbers, 666);
    }
    
    private static void genData(int[] a, long seed) {
        Random r = new Random(seed);
        for (int i = 0; i < a.length; ++ i) {
            numbers[i] = r.nextInt(MAX);
        }
    }
    
    private final int[] a;

    public Test(int[] a) {
        this.a = a;
    }
    
    private void quicksort(int p, int q) {
        int i = p;
        int j = q;
        int v = a[(p + q) / 2];
        do {
            while (a[i] < v) { ++ i; }
            while (v < a[j]) { -- j; }
            
            if (i <= j) {
                int temp = a[i];
                a[i] = a[j];
                a[j] = temp;
                ++i;
                --j;
            }
        } while (i <= j);
        if (p < j) {
            quicksort(p, j);
        }
        if (i < q) {
            quicksort(i, q);
        }
    }

    public static void main(String[] args) {
        
        Test t = new Test(numbers);
        
        t.run();
        
        for (int n : numbers) {
            System.out.printf("%d ", n);
        }
        System.out.println();
    }

    @Override
    public void run() {
        quicksort(0, a.length - 1);
    }

}
