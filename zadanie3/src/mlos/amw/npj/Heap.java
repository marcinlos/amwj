package mlos.amw.npj;

import java.util.Arrays;


public class Heap {

    private final int[] heap;
    private final int areaSize;
    
    private int base = 1;
    private int allocOffset;

    public Heap(int heapSize) {
        if (heapSize % 8 != 1) {
            throw new RuntimeException("Invalid heap size (size mod 8 != 1)");
        }
        log("Heap size = %d", heapSize);
        this.areaSize = (heapSize - 1) / 2;
        this.heap = new int[heapSize];
    }
    
    private int physical(int address) {
        return address;
    }
    
    public int getBase() {
        return base;
    }
    
    public void setBase(int base) {
        this.base = base;
    }
    
    public int get(int address) {
        return heap[physical(address)];
    }
    
    public void put(int address, int val) {
        heap[physical(address)] = val;
    }
    
    public void flip() {
        base = areaSize - (base - 1) + 1;
        allocOffset = 0;
    }
    
    public int getAreaSize() {
        return areaSize;
    }
    
    public int[] getHeap() {
        return heap;
    }
    
    public void writeString(int address, String s) {
        int ptr = physical(address);
        for (int i = 0; i < s.length(); ++i) {
            heap[ptr + i] = s.charAt(i);
        }
    }
    
    public String readString(int address, int size) {
        int ptr = physical(address);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < size; ++i) {
            char c = (char) heap[ptr + i];
            sb.append(c);
        }
        return sb.toString();
    }
    
    public static void log(String msg, Object... args) {
        System.out.printf(">> " + msg + "\n", args);
    }
    
    public int alloc(int size) {
        int newOffset = allocOffset + size;
        int offset = allocOffset;
        
        log("Allocating %d, heap offset %d -> %d", size, allocOffset, newOffset);
        if (newOffset > areaSize) {
            throw new OutOfMemoryError();
        }
        allocOffset = newOffset;
        return base + offset;
    }
    
    public void memcpy(int dest, int src, int size) {
        System.arraycopy(heap, src, heap, dest, size);
    }
    
    public void memset(int dest, int val, int size) {
        Arrays.fill(heap, dest, dest + size, val);
    }
    
}
