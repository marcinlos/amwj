package mlos.amw.npj.ast;

public interface Statement {

    void accept(Visitor v);
    
}
