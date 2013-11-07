package mlos.amw.npj.ast;

public interface ValueVisitor {

    void visitInt(int value);
    
    void visitString(String value);
    
    void visitNull();
    
    void visitVar(Deref deref);
}
