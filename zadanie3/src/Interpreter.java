import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import mlos.amw.npj.ast.Statement;
import mlos.amw.npj.parser.NPJParser;

public class Interpreter {

    private static final int DEFAULT_HEAP = 32;
    private static final String HEAP_SIZE_PROPERTY = "npj.heap.size";

    private static List<Statement> parseProgram(String sourceFile) {
        InputStream programStream = getProgramStream(sourceFile);
        try {
            return NPJParser.parse(programStream);
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(2);
        } finally {
            try {
                programStream.close();
            } catch (IOException e) {
                e.printStackTrace(System.err);
            }
        }
        return null;
    }

    /**
     * Opens program file and returns program code input stream.
     * 
     * @param programFile
     *            path to program file
     * @return input stream for program code
     */
    private static InputStream getProgramStream(String programFile) {
        InputStream programStream = null;
        try {
            programStream = new FileInputStream(programFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open program file: " + programFile
                    + ", exiting.");
            System.exit(2);
        }
        return programStream;
    }

    /**
     * Retrieves heap size from the system environment
     * @return
     */
    private static int getHeapSize() {
        String heapSizeProp = System.getProperty(HEAP_SIZE_PROPERTY);
        if (heapSizeProp == null) {
            return DEFAULT_HEAP;
        } else
            try {
                return Integer.parseInt(heapSizeProp);
            } catch (NumberFormatException e) {
                System.err.printf("Cannot parse heap size ('%s')\n",
                        heapSizeProp);
                System.exit(1);
                return -1;
            }
    }

    public static void main(String[] args) {
        // make sure we got one and only one parameter
        if (args.length != 1) {
            System.out
                    .println("Wrong number of arguments - pass program file name");
            System.exit(1);
        }

        // get heap size
        int heapSize = getHeapSize();

        System.out.printf("Heap size = %d\n", heapSize);
        List<Statement> program = parseProgram(args[0]);
        
        VM vm = new VM(heapSize, program);
        vm.run();
    }
}
