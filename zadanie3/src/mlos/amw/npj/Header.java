package mlos.amw.npj;

public class Header {
    
    public static final int TYPE_MASK           = 0x00ffffff;
    
    public static final int PROXY               = 0x80000000;
    public static final int COPIED              = 0x40000000;
    public static final int FRESH_COPY          = 0x00010000;

    public static int type(int header) {
        return header & TYPE_MASK;
    }
    
    public static boolean isProxy(int header) {
        return (header & PROXY) != 0;
    }
    
    public static boolean isFixedAfterCopy(int header) {
        return (header & FRESH_COPY) == 0;
    }
}
