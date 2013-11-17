import java.util.ArrayDeque;
import java.util.Deque;



public class Second {
    
    static int a;
    static {
        a = 3;
    } 
    
    // Quicksort
    
    public static class Range {
        public final int p;
        public final int r;
        
        public Range(int p, int r) {
           this.p = p;
           this.r = r;
        }
    }
    
    public int sort(int[] array) {
        return sort(array, 0, array.length);
    }
    
    public int sort(int[] array, int p, int r) {
        Deque<Range> parts = new ArrayDeque<Range>();
        Range full = range(p, r);
        parts.add(full);
        
        while (!parts.isEmpty()) {
            Range w = parts.poll();
            if (isBigEnough(w)) {
                int q = partition(array, w.p, w.r);
                Range left = range(w.p, q + 1);
                Range right = range(q + 1, w.r);
                parts.add(left);
                parts.add(right);
            }
        }
        return r - p;
    }
    
    public boolean isBigEnough(Range range) {
        return range.r - range.p > 1;
    }
    
    public Range range(int p, int r) {
        return new Range(p, r);
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
    
    // Morris' inorder traversal
    
    public static class Tree {
        public final int v;
        public Tree left;
        public Tree right;
        
        public Tree(int v, Tree left, Tree right) {
            this.v = v;
            this.left = left;
            this.right = right;
        }
    }
    
    public Tree leaf(int v) {
        return new Tree(v, null, null);
    }
    
    public boolean hasRight(Tree tree) {
        return tree.right != null;
    }
    
    public boolean hasLeft(Tree tree) {
        return tree.left != null;
    }
     
    public Tree insert(Tree root, int value) {
        Tree child = leaf(value);
        Tree pred = findPred(root, value);
        
        if (pred != null) {
            link(pred, child);
            return root;
        } else {
            return child;
        }
    }
    
    public Tree findPred(Tree root, int value) {
        Tree pred = null;
        
        while (root != null) {
            pred = root;
            root = chooseSide(root, value);
        }
        return pred;
    }
    
    public void link(Tree pred, Tree child) {
        if (child.v < pred.v) {
            pred.left = child;
        } else {
            pred.right = child;
        }
    }
    
    public Tree chooseSide(Tree n, int value) {
        return value < n.v ? n.left : n.right;
    }
    
    public void morris(Tree root) {
        Tree pred = null;
        while (root != null) {
            root = nextNode(pred, root);
            visit(root);
            pred = root;
            root = root.right;
        }
    }
    
    public Tree nextNode(Tree pred, Tree root) {
        while (hasLeft(root) && !checkThread(pred, root)) {
            root = root.left;
        }
        return root;
    }
    
    public boolean checkThread(Tree pred, Tree root) {
        Tree q = root.left;
        while (q != pred && hasRight(q)) {
            q = q.right;
        }
        boolean thread = hasRight(q);
        q.right = thread ? null : root;
        return thread;
    }
    

    public void visit(Tree root) {
        System.out.print(root.v + " ");
    }
}
