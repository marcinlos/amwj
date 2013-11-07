package mlos.amw.npj.ast;

public interface RValue {

    void accept(ValueVisitor visitor);
    
}
