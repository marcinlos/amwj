
import java.util.Collection;
import java.util.Map;

public class NPJ {
    
    public static void print(String string) {
        System.out.println(string);
    }

    public static void collect(int[] heap, Collector collector, Map<Object, Object> params) {
        collector.collect(heap, params);
    }

	/*
	  typeTVariables - kolekcja wartosci pola "data" wszystkich zywych obiektow typu T
	  typeSVariables - kolekcja wartosci wszystkich zywych obiektow typu S 
	*/
    public static void heapAnalyze(Collection<Integer> typeTVariables, Collection<String> typeSVariables) {
        System.err.println("= = = = = = = = = = = = = = = = = = = = = =");
        System.err.println("T:");
        for (Integer n : typeTVariables) {
            System.err.println("   " + n);
        }
        System.err.println("S:");
        for (String s : typeSVariables) {
            System.err.println("   " + s);
        }
        System.err.println("= = = = = = = = = = = = = = = = = = = = = =");
    }
}
