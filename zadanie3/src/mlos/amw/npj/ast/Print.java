package mlos.amw.npj.ast;

public class Print implements Statement {

    public final String string;
    public final PrintSubject subject;
    
    private Print(String string, PrintSubject subject) {
        this.string = string;
        this.subject = subject;
    }
    
    public boolean var() {
        return subject == PrintSubject.VAR;
    }
    
    public boolean literal() {
        return subject == PrintSubject.LITERAL;
    }
    
    public static Print var(String name) {
        return new Print(name, PrintSubject.VAR);
    }

    public static Print literal(String literal) {
        return new Print(literal, PrintSubject.LITERAL);
    }

    @Override
    public void accept(Visitor v) {
        switch (subject) {
        case LITERAL: v.visitPrintLiteral(string); break;
        case VAR: v.visitPrintVar(string); break;
        }
    }
}
