import java.util.Comparator;
import java.util.LinkedList;
import java.util.Random;

/**
 * Your implementation of various sorting algorithms.
 *
 * @author Parker Hyde
 * @version 1.0
 */
public class Sorting {

    /**
     * Implement insertion sort.
     *
     * It should be:
     *  in-place
     *  stable
     *
     * Have a worst case running time of:
     *  O(n^2)
     *
     * And a best case running time of:
     *  O(n)
     *
     * @throws IllegalArgumentException if the array or comparator is null
     * @param <T> data type to sort
     * @param arr the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     */
    public static <T> void insertionSort(T[] arr, Comparator<T> comparator) { 
        if (arr == null) {
            throw new IllegalArgumentException("Cannot sort a null array");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Cannot sort input array with null comparator");
        }
        for (int i = 1; i < arr.length; i++) {
            T next = arr[i];
            int j = i;
            while (j > 0 && comparator.compare(arr[j-1], next) > 0) {
                arr[j] = arr[j-1];
                j--;
            }
            arr[j] = next;
        }
    }

    /**
     * Implement selection sort.
     *
     * It should be:
     *  in-place
     *  unstable
     *
     * Have a worst case running time of:
     *  O(n^2)
     *
     * And a best case running time of:
     *  O(n^2)
     *
     * @throws IllegalArgumentException if the array or comparator is null
     * @param <T> data type to sort
     * @param arr the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     */
    public static <T> void selectionSort(T[] arr, Comparator<T> comparator) {
        if (arr == null) {
            throw new IllegalArgumentException("Cannot sort a null array");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Cannot sort input array with null comparator");
        }
        for (int i = 0; i < arr.length; i++) {
            int minIndex = i;
            for (int j = i+1; j < arr.length; j++) {
                if (comparator.compare(arr[j], arr[minIndex]) < 0) {
                    minIndex = j;
                }
            }
            T tmp = arr[i];
            arr[i] = arr[minIndex];
            arr[minIndex] = tmp;
        }
    }

    /**
     * Implement merge sort.
     *
     * It should be:
     *  out-of-place
     *  stable
     *
     * Have a worst case running time of:
     *  O(n log n)
     *
     * And a best case running time of:
     *  O(n log n)
     *
     * You can create more arrays to run mergesort, but at the end, everything
     * should be merged back into the original T[] which was passed in.
     *
     * When splitting the array, if there is an odd number of elements, put the
     * extra data on the right side.
     *
     * @throws IllegalArgumentException if the array or comparator is null
     * @param <T> data type to sort
     * @param arr the array to be sorted
     * @param comparator the Comparator used to compare the data in arr
     */
    public static <T> void mergeSort(T[] arr, Comparator<T> comparator) {
        if (arr == null) {
            throw new IllegalArgumentException("Cannot sort a null array");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Cannot sort input array with null comparator");
        }
        if (arr.length < 2) {
            return;
        }
        int m = arr.length / 2;
        T[] left = copyOfRange(arr, 0, m);
        T[] right = copyOfRange(arr, m, arr.length);
        mergeSort(left, comparator);
        mergeSort(right, comparator);
        merge(left, right, arr, comparator);
    }

    /*
     * Merge Sort helper method
     * Merges and collects the elements of left and right into the result array
     * The elements are merged to result in O(n) time
     *
     * @ param left the left subarray to merge
     * @ param right the right subarray to merge
     * @ param result the result array to merge elements into
     */
    private static <T> void merge(T[] left, T[] right, T[] result, Comparator<T> comparator) {
        int i = 0;
        int j = 0;
        int k = 0;
        while (i < left.length && j < right.length) {
            result[k++] = comparator.compare(left[i], right[j]) < 0 ? left[i++] : right[j++];
        }
        while (i < left.length) {
            result[k++] = left[i++];
        }
        while (j < right.length) {
            result[k++] = right[j++];
        }
    }

    /**
     * Implement quick sort.
     *
     * Use the provided random object to select your pivots. For example if you
     * need a pivot between a (inclusive) and b (exclusive) where b > a, use
     * the following code:
     *
     * int pivotIndex = rand.nextInt(b - a) + a;
     *
     * If your recursion uses an inclusive b instead of an exclusive one,
     * the formula changes by adding 1 to the nextInt() call:
     *
     * int pivotIndex = rand.nextInt(b - a + 1) + a;
     *
     * It should be:
     *  in-place
     *  unstable
     *
     * Have a worst case running time of:
     *  O(n^2)
     *
     * And a best case running time of:
     *  O(n log n)
     *
     * Make sure you code the algorithm as you have been taught it in class.
     * There are several versions of this algorithm and you may not receive
     * credit if you do not use the one we have taught you!
     *
     * @throws IllegalArgumentException if the array or comparator or rand is
     * null
     * @param <T> data type to sort
     * @param arr the array that must be sorted after the method runs
     * @param comparator the Comparator used to compare the data in arr
     * @param rand the Random object used to select pivots
     */
    public static <T> void quickSort(T[] arr, Comparator<T> comparator, Random rand) {
        if (arr == null) {
            throw new IllegalArgumentException("Cannot sort a null array");
        }
        if (comparator == null) {
            throw new IllegalArgumentException("Cannot sort input array with null comparator");
        }
        if (rand == null) {
            throw new IllegalArgumentException("Cannot use null rand to sort input array");
        }
        quickSortRange(arr, 0, arr.length, comparator, rand);
    }

    /*
     * Helper method to apply quick sort over a given range
     * 
     * @param arr the array to sort over a given range
     * @param start the start index of the range (inclusive)
     * @param end the end index of the range (exclusive)
     * @param comparator the Comparator used to compare the data in arr
     * @param rand the Random object used to select pivots
     */
    private static <T> void quickSortRange(
        T[] arr, 
        int start, 
        int end, 
        Comparator<T> comparator, 
        Random rand
    ) {
        if (start < end-1) {
            int pivotIdx = rand.nextInt(end - start) + start;
            swap(end-1, pivotIdx, arr);
            int q = partition(arr, start, end, comparator);
            quickSortRange(arr, start, q, comparator, rand);
            quickSortRange(arr, q+1, end, comparator, rand);
        }
    }

    /**
     * Partitions the input array by pivot over the provided range
     * The pivot is assumed to be the last element in the range (index end-1)
     *
     * @param arr the array to partition over a given range
     * @param start the start index of the range (inclusive)
     * @param end the end index of the range (exclusive)
     * @param comparator the Comparator used to compare the data in arr
     * @return the partitioning index
     */
    private static <T> int partition(T[] arr, int start, int end, Comparator<T> comparator) {
        for (int i = start; i < end-1; i++) {
            if (comparator.compare(arr[i], arr[end-1]) < 0) {
                swap(start, i, arr);
                start++;
            }
        }
        swap(start, end-1, arr);
        return start;
    }

    /**
     * Helper method to swap two items by index in the given array
     *
     * @param i the first index to swap
     * @param j the second index to swap
     * @param arr the array containing the elements to swap
     */
    private static <T> void swap(int i, int j, T[] arr) {
        T tmp = arr[i];
        arr[i] = arr[j];
        arr[j] = tmp;
    }
    /**
     * Implement LSD (least significant digit) radix sort.
     *
     * Make sure you code the algorithm as you have been taught it in class.
     * There are several versions of this algorithm and you may not get full
     * credit if you do not implement the one we have taught you!
     *
     * Remember you CANNOT convert the ints to strings at any point in your
     * code! Doing so may result in a 0 for the implementation.
     *
     * It should be:
     *  out-of-place
     *  stable
     *
     * Have a worst case running time of:
     *  O(kn)
     *
     * And a best case running time of:
     *  O(kn)
     *
     * You are allowed to make an initial O(n) passthrough of the array to
     * determine the number of iterations you need.
     *
     * Refer to the PDF for more information on LSD Radix Sort.
     *
     * You may use {@code java.util.ArrayList} or {@code java.util.LinkedList}
     * if you wish, but it may only be used inside radix sort and any radix sort
     * helpers. Do NOT use these classes with other sorts.
     *
     * Do NOT use anything from the Math class except Math.abs().
     *
     * @throws IllegalArgumentException if the array is null
     * @param arr the array to be sorted
     */
    public static void lsdRadixSort(int[] arr) {
        if (arr == null) {
            throw new IllegalArgumentException("Cannot sort a null array");
        }
        int k = maxDigits(arr);
        int place = 1;
        while (k-- > 0) {
            countingSortByDigitPlace(arr, place);
            place *= 10;
        }
    }

    /*
     * Helper method for lsdRadixSort
     * Returns the maximum digits count for any number present in the input array
     *
     * @param arr the array to inspect
     */
    private static int maxDigits(int[] arr) {
        int max = 0;
        for (int n : arr) {
            int count = 0;
            while (n > 0) {
                count++;
                n /= 10;
            }
            if (count > max) {
                max = count;
            }
        }
        return max;
    }

    /*
     * Sorts the input array using counting sort on the provided digits "place"
     * place = 1  => sort on 1's place
     * place = 10 => sort on 10's place
     * etc.
     *
     * @param arr the int array to sort
     * @param place the place on which to sort
     */
    private static void countingSortByDigitPlace(int[] arr, int place) {
        int[] count = new int[19];
        for (int n : arr) {
            int score = scoreDigit(n, place);
            count[score]++;
        }
        for (int i = 1; i < count.length; i++) {
            count[i] += count[i-1];
        }
        int[] tmp = new int[arr.length];
        for (int i = arr.length-1; i >= 0; i--) {
            int score = scoreDigit(arr[i], place);
            int position = count[score]-1;
            tmp[position] = arr[i];
            count[score]--;
        }
        for (int i = 0; i < arr.length; i++) {
            arr[i] = tmp[i];
        }
    }

    /*
     * Returns an ordering score for input n 
     * score caluclated on a scale of 0-18 (inclusive) using
     * the digit in the desired "place"
     *
     * @param n the int to score
     * @param place the digit's place to score
     * @return the ordering score of n
     */
    private static int scoreDigit(int n, int place) {
        int absVal = Math.abs(n);
        int sign = n / absVal;
        int absDig = (absVal / place) % 10;
        int dig = sign * absDig;
        return dig + 9;
    }

    /*
     * Creates a copy of the input array over the given range
     *
     * @param arr the arr to be copied
     * @param start the start of the range (inclusive)
     * @param end the end of the range (exclusive)
     * @return copy of the input over indicies start - end
     */
    private static <T> T[] copyOfRange(T[] arr, int start, int end) {
        int length = end - start;
        T[] result = (T[]) new Object[length];
        for (int i = 0; i < length; i++) {
            result[i] = arr[start + i]; 
        } 
        return result;
    }

}
