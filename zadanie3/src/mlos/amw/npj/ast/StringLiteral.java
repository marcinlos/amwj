package mlos.amw.npj.ast;

public class StringLiteral implements RValue {
    
    public final String value;

    public StringLiteral(String value) {
        this.value = value;
    }

}