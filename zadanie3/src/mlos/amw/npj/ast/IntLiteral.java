package mlos.amw.npj.ast;

public class IntLiteral implements RValue {
    
    public final int value;

    public IntLiteral(int value) {
        this.value = value;
    }
    
    public static IntLiteral from(String s) {
        return new IntLiteral(Integer.parseInt(s));
    }

    @Override
    public void accept(ValueVisitor visitor) {
        visitor.visitInt(value);
    }
    
    @Override
    public String toString() {
        return String.valueOf(value);
    }

}
