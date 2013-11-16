

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
    
    public void inc() {
        ++ visited;
    }

}
