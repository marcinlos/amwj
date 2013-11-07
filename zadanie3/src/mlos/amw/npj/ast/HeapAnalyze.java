package mlos.amw.npj.ast;

public enum HeapAnalyze implements Statement {
    VALUE;

    @Override
    public void accept(Visitor v) {
        v.visitHeapAnalyze();
    }
}
