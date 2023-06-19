import java.util.ArrayList;
import java.util.NoSuchElementException;

/**
 * Your implementation of a max heap.
 *
 * @author Parker Hyde
 * @version 1.0
 */
public class MaxHeap<T extends Comparable<? super T>> {

    // DO NOT ADD OR MODIFY THESE INSTANCE/CLASS VARIABLES.
    public static final int INITIAL_CAPACITY = 13;

    private T[] backingArray;
    private int size;

    /**
     * Creates a MaxHeap with an initial capacity of INITIAL_CAPACITY
     * for the backing array.
     *
     * Use the constant field provided. Do not use magic numbers!
     */
    public MaxHeap() {
        backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Creates a properly ordered heap from a set of initial values.
     *
     * You must use the BuildMaxHeap algorithm that was taught in lecture! Simply
     * adding the data one by one using the add method will not get any credit.
     * As a reminder, this is the algorithm that involves building the heap
     * from the bottom up by repeated use of downMaxHeap operations.
     *
     * The data in the backingArray should be in the same order as it appears
     * in the passed in ArrayList before you start the Build MaxHeap Algorithm.
     *
     * The backingArray should have capacity 2n + 1 where n is the
     * number of data in the passed in ArrayList (not INITIAL_CAPACITY from
     * the interface). Index 0 should remain empty, indices 1 to n should
     * contain the data in proper order, and the rest of the indices should
     * be empty.
     *
     * @param data a list of data to initialize the heap with
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public MaxHeap(ArrayList<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Attempting to insert null ArrayList into MaxHeap");
        }
        if (data.contains(null)) {
            throw new IllegalArgumentException("Attempting to insert ArrayList with null data into MaxHeap");
        }
        int n = data.size();
        backingArray = (T[]) new Comparable[2*n + 1];
        for (int i = 0; i < n; i++) {
            backingArray[i+1] = data.get(i);
        }
        size = n;
        heapifyBackingArray();
    }

    /**
     * Uses the buildheap algorithm to build the tree from the bottom up.
     * Ensures the heap property by recursively replacing out of order parent nodes with max(child1, child2). 
     * smaller nodes are effectively sifted down the tree. 
     */
    private void heapifyBackingArray() {
        int startIdx = size / 2;
        for (int i = startIdx; i > 0; i--) {
            heapify(i);
        }
    }

    /**
     * Recursively ensures the heap property is satisfied for a given node and it's children.
     */
    private void heapify(int index) {
        int leftIndex = index*2;
        int rightIndex = index*2+1;
        if (leftIndex > size) {
            // no children
            return;
        }
        if (leftIndex == size) {
            // left child only
            if (backingArray[index].compareTo(backingArray[leftIndex]) < 0) {
                // parent < left child
                swap(index, leftIndex);
            }
            return;
        }
        // left and right child
        T parent = backingArray[index];
        T left = backingArray[leftIndex];
        T right = backingArray[rightIndex];
        if (parent.compareTo(left) >= 0 && parent.compareTo(right) >= 0) {
            return;
        }
        if (left.compareTo(right) >= 0) {
            swap(index, leftIndex);
            heapify(leftIndex);
        } else {
            swap(index, rightIndex);
            heapify(rightIndex);
        }
    }

    /**
     * Ensures the heap property is satisfied for a given node.
     *
     * @param i first index to swap
     * @param i second index to swap
     */
    private void swap(int i, int j) {
        T tmp = backingArray[i];
        backingArray[i] = backingArray[j];
        backingArray[j] = tmp;
    }

    /**
     * Adds an item to the heap. If the backing array is full and you're trying
     * to add a new item, then double its capacity.
     *
     * @throws IllegalArgumentException if the item is null
     * @param item the item to be added to the heap
     */
    public void add(T item) {
        if (item == null) {
            throw new IllegalArgumentException("Attempting to add null data to MaxHeap");
        }
        checkCapacity();
        backingArray[++size] = item;
        siftUp(size);
    }

    /**
     * Checks the capacity of the backing array
     * doubles the capacity if backingArray is full
     */
    private void checkCapacity() {
        int capacity = backingArray.length;
        int utilized = size+1;
        if (utilized < capacity) {
            return;
        }
        T[] newBackingArray = (T[]) new Object[2*capacity];
        for (int i = 0; i <= size; i++) {
            newBackingArray[i] = backingArray[i];
        }
        backingArray = newBackingArray;
    }

    /**
     * sifts (bubbles) the node at index up the tree until the heap property is satisfied
     *
     * @param index the index of the node to sift
     */
    private void siftUp(int index) {
        if (index == 1) {
            return;
        }
        int parentIndex = index / 2;
        if (backingArray[index].compareTo(backingArray[parentIndex]) > 0) {
            swap(index, parentIndex);
            siftUp(parentIndex);
        }
    }


    /**
     * Removes and returns the max item of the heap. As usual for array-backed
     * structures, be sure to null out spots as you remove. Do not decrease the
     * capacity of the backing array.
     *
     * @throws java.util.NoSuchElementException if the heap is empty
     * @return the removed item
     */
    public T remove() {
        if (size == 0) {
            throw new NoSuchElementException("Attempting to remove the max item from an empty MaxHeap");
        }
        T removed = backingArray[1];
        backingArray[1] = null;
        swap(1, size);
        size--;
        heapify(1);
        return removed;
    }


    /**
     * Returns the maximum element in the heap.
     *
     * @return the maximum element, null if the heap is empty
     */
    public T getMax() {
        if (size == 0) {
            return null;
        }
        return backingArray[1];
    }

    /**
     * Returns if the heap is empty or not.
     *
     * @return true if the heap is empty, false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the heap and rests the backing array to a new array of capacity
     * {@code INITIAL_CAPACITY}.
     */
    public void clear() {
        backingArray = (T[]) new Comparable[INITIAL_CAPACITY];
        size = 0;
    }

    /**
     * Returns the size of the heap.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return number of items in the heap
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }

    /**
     * Returns the backing array of the heap.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the backing array of the heap
     */
    public Object[] getBackingArray() {
        // DO NOT MODIFY THIS METHOD!
        return backingArray;
    }
}
