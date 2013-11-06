import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Interpreter {

    private static final int DEFAULT_HEAP = 32;
    
    private int[] heap;
    private int maxHeapSize;
    private int heapSize;
    
    /**
     * Initializes Njp interpreter with specified program and heap size.
     * 
     * @param programFile
     *            path to program file
     * @param heapSize
     *            heap size
     */
    public Interpreter(String programFile, int heapSize) {
        System.out.printf("Heap size = %d\n", heapSize);
        InputStream programStream = getProgramStream(programFile);
    }

    /**
     * Opens program file and returns program code input stream.
     * 
     * @param programFile
     *            path to program file
     * @return input stream for program code
     */
    private InputStream getProgramStream(String programFile) {
        InputStream programStream = null;
        try {
            programStream = new FileInputStream(programFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open program file: " + programFile + ", exiting.");
            System.exit(2);
        }
        return programStream;
    }

	private static int getHeapSize() {
	    String heapSizeProp = System.getenv("npj.heap.size");
	    if (heapSizeProp == null) {
	        return DEFAULT_HEAP;
	    } else try {
	        return Integer.parseInt(heapSizeProp);
	    } catch (NumberFormatException e) {
	        System.err.printf("Cannot parse heap size ('%s')\n", heapSizeProp);
	        System.exit(1);
	        return -1;
	    }
	}
	
    public static void main(String[] args) {
        // make sure we got one and only one parameter
        if (args.length != 1) {
            System.out.println("Wrong number of arguments - pass program file name");
            System.exit(1);
        }

        // get heap size
        int heapSize = getHeapSize();

        // initialize Njp VM with program file name, heap size and GC
        Interpreter interpreter = new Interpreter(args[0], heapSize);
    }
}
