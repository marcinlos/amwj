package mlos.amw.npj.ast;

public class Assignment implements Statement {
    
    public final Deref lhs;
    public final RValue rhs;

    public Assignment(Deref lhs, RValue rhs) {
        this.lhs = lhs;
        this.rhs = rhs;
    }

}