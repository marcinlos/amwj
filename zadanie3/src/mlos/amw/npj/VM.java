package mlos.amw.npj;

import java.util.List;

import mlos.amw.npj.ast.Statement;

public class VM implements Runnable {
    
    private final int[] heap;
    private final int heapSize;

    private final List<Statement> program;

    public VM(int heapSize, List<Statement> program) {
        this.heapSize = heapSize;
        this.program = program;
        this.heap = new int[heapSize];
    }
    
    @Override
    public void run() {
        
    }

}
