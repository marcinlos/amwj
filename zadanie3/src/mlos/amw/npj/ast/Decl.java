package mlos.amw.npj.ast;

public class Decl implements Statement {
    
    public final String name;
    public final VarType type;
    public final String value;
    
    private Decl(String name, VarType type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }
    
    public static Decl newTVar(String name) {
        return new Decl(name, VarType.T, null);
    }
    
    public static Decl newSVar(String name) {
        return newSVar(name, null);
    }

    public static Decl newSVar(String name, String value) {
        return new Decl(name, VarType.S, value);
    }

    @Override
    public void accept(Visitor v) {
        switch (type) {
        case S: v.visitDeclS(name, value); break;
        case T: v.visitDeclT(name); break;
        }
    }
    
    @Override
    public String toString() {
        if (type == VarType.S) {
            String val = value == null ? "NULL" : ("\"" + value + "\"");
            return String.format("VarDeclS %s %s", name, val);
        } else {
            return "VarDeclT " + name;
        }
    }
    
}
