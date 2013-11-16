

public class Second {
    
    static int a;
    static {
        a = 3;
    } 
    
    public int val;
    public int visited;
    
    public int traverse(int... graph) {
        for (int n : graph) {
            val += visit(n);
        }
        return val + visited;
    }
    
    public int visit(int a) {
        inc();
        return a * a;
    }
    
    public int inc() {
        int b = 3;
        return ++ visited;
    }
    
    public int args(int a, int b, int c) {
//        System.out.printf("a=%d, b=%d, c=%d\n", a, b, c);
        a = 11;
        b = 21;
        c = 31;
        String dupa = "sdfsdf";
        System.out.println(dupa);
        int kk = a + b;
        int d = a +  c;
//        System.out.printf("a=%d, b=%d, c=%d\n", a, b, c);
        return kk - a + d;
    }

}
