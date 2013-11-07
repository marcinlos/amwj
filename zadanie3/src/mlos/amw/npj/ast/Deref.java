package mlos.amw.npj.ast;

public class Deref implements RValue {
    
    public final String name;
    public final Deref target;
    
    public Deref(String name, Deref target) {
        this.name = name;
        this.target = target;
    }
    
    public Deref(String name) {
        this(name, null);
    }

    @Override
    public void accept(ValueVisitor visitor) {
        visitor.visitVar(this);
    }
    
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(name);
        Deref d = this;
        while (d.target != null) {
            d = d.target;
            sb.append('.').append(d.name);
        }
        return sb.toString();
    }

}
