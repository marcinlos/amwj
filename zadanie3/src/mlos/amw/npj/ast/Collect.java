package mlos.amw.npj.ast;

public enum Collect implements Statement {
    VALUE;

    @Override
    public void accept(Visitor v) {
        v.visitCollect();
    }
    
    @Override
    public String toString() {
        return "Collect";
    }
}
