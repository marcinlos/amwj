

public class First {

    static int[] a = { 1, 4, 2, 5, 6, 3, 15, 7, 13, 10, 9 };
    
    public static void main(String[] args) {
        Second s = new Second();
        
        int[] a1 = a.clone();
        s.sort(a1);
        for (int n : a1) { 
            System.out.print(n + " "); 
        } 
        
        System.out.println("\n= = = = = = = = = = = = = = = = = = = = = = =");

        int[] a2 = a.clone();
        Second.Tree t = null;
        for (int n : a2) {
            t = s.insert(t, n);
        }
        s.morris(t);

        System.out.println();
    }

}
 