/**
 * Lab 4: Generics <br />
 * The {@code GenericHeap} class
 */
import java.util.ArrayList;

public class GenericHeap {
    /**
     * The heap sort procedure
     * @param array     {@code <E extends Comparable<E>>[]} the array to be sorted
     * @return          {@code <E extends Comparable<E>>[]} the sorted array
     */
    public static <E extends Comparable<E>> E[] heapSort(E[] array) {
        // TODO: Lab 4 Part 2-4 -- GenericHeap, return a sorted array
        int n = array.length;
        buildMaxHeap(array, n);
        
        for (int i = n-1; i >= 0; i--) {
        	E temp = array[0];
        	array[0] = array[i];
        	array[i] = temp;
            heapify(array, i, 0);
        }

        return array;
    }

    public static <E extends Comparable<E>> void heapify(E[] array, int n, int i) {
        int largest = i;
        int left = 2*i + 1;
        int right = 2*i + 2;

        if (left < n && array[left].compareTo(array[largest]) > 0) {
            largest = left;
        }

        if (right < n && array[right].compareTo(array[largest]) > 0) {
            largest = right;
        }

        if (largest != i) {
        	E temp = array[i];
        	array[i] = array[largest];
        	array[largest] = temp;
            heapify(array, n, largest);
        }  
    }

    public static <E extends Comparable<E>> void buildMaxHeap(E[] array, int n) {
        for (int i = n/2 - 1; i >= 0; i--) {
            heapify(array, n, i);
        }
    }

    /**
     * Main entry: test the HeapSort
     * @param args      {@code String[]} Command line arguments
     */
    public static void main(String[] args) {
        // Sort an array of integers
        Integer[] numbers = new Integer[10];
        for (int i = 0; i < numbers.length; i++)
            numbers[i] = (int) (Math.random() * 200);
        heapSort(numbers);
        for (int n: numbers)
            System.out.print(n + " ");
        System.out.println();

        // Sort an array of strings
        String[] strs = new String[10];
        for (int i = 0; i < strs.length; i++)
            strs[i] = String.format("%c", (int) (Math.random() * 26 + 65));
        heapSort(strs);
        for (String s: strs)
            System.out.print(s + " ");
        System.out.println();
    }

}
