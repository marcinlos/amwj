import java.util.Arrays;
import java.util.List;


public class Test2 {

    boolean z = true;
    char c = 'x';
    byte b = 123;
    short s = 14567;
    int i = 1234567890;
    long j = 1234567890987654321L;
    float f = 1.23f;
    double d = 1.234567;
    List<Integer> list = Arrays.asList(1, 2, 3);
    
    private static void dummy(Object o) { }
    
    public static void main(String[] args) {
        Test2 t = new Test2();
        dummy(t.z);
        dummy(t.c);
        dummy(t.b);
        dummy(t.s);
        dummy(t.i);
        dummy(t.j);
        dummy(t.f);
        dummy(t.d);
        dummy(t.list);
    }

}
