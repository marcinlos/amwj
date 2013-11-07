package mlos.amw.npj.ast;

public enum Null implements RValue {
    VALUE;

    @Override
    public void accept(ValueVisitor visitor) {
        visitor.visitNull();
    }
    
    @Override
    public String toString() {
        return "NULL";
    }
}
