import java.util.Arrays;
import java.util.List;


public class Test {
    
    private static boolean z() {
        return true;
    }
    
    private static char c() { 
        return 'x';
    }
    
    private static byte b() {
        return 113;
    }
    
    private static short s() {
        return 12345;
    }
    
    private static int i() {
        return 123456789;
    }
    
    private static long j() {
        return 1234567890987654321L;
    }
    
    private static float f() {
        return 1.23f;
    }
    
    private static double d() {
        return 1.23456;
    }
    
    private static List<Integer> list() {
        return Arrays.asList(1, 2, 3);
    }

    public static void main(String[] args) {
        z();
        c();
        b();
        s();
        i();
        j();
        f();
        d();
        list();
    }

}
