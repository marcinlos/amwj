package mlos.amw.npj;

public class Vars {
    
    public static final int TYPE_MASK      = 0x00ffffff;
    
    public static final int PROXY          = 0x80000000;
    public static final int VISITED        = 0x40000000;
    
    public static final int TYPE_S         = 0x000001;
    public static final int TYPE_T         = 0x000002;

    public static int type(int header) {
        return header & TYPE_MASK;
    }
    
    public static boolean isProxy(int header) {
        return (header & PROXY) != 0;
    }
    
    public static boolean isSVar(int header) {
        return type(header) == TYPE_S;
    }

    public static boolean isTVar(int header) {
        return type(header) == TYPE_T;
    }
    
}
