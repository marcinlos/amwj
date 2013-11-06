package mlos.amw.npj.ast;

public class IntLiteral implements RValue {
    
    public final int value;

    public IntLiteral(int value) {
        this.value = value;
    }
    
    public static IntLiteral from(String s) {
        return new IntLiteral(Integer.parseInt(s));
    }

}
