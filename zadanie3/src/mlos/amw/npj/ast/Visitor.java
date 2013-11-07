package mlos.amw.npj.ast;

public interface Visitor {

    void visitCollect();
    
    void visitHeapAnalyze();

    void visitDeclS(String name, String value);
    
    void visitDeclT(String name);
    
    void visitAssignment(Deref target, RValue value);
    
    void visitPrintVar(String name);
    
    void visitPrintLiteral(String text);
}
