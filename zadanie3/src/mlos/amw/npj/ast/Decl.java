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
    
}
