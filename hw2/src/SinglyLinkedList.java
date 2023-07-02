/**
 * Your implementation of a circular singly linked list.
 *
 * @author Parker Hyde
 * @version 1.0
 */
public class SinglyLinkedList<T> {
    // Do not add new instance variables or modify existing ones.
    private LinkedListNode<T> head;
    private int size;


    /**
     * Adds the element to the index specified.
     *
     * Adding to indices 0 and {@code size} should be O(1), all other cases are
     * O(n).
     *
     * @param index the requested index for the new element
     * @param data the data for the new element
     * @throws IndexOutOfBoundsException if index is negative or
     * index > size
     * @throws IllegalArgumentException if data is null
     */
    public void addAtIndex(int index, T data) {
        if (data == null) {
            throw new IllegalArgumentException("Attempting to add null data to SinglyLinkedList");
        }
        if (index < 0 || index > size) {
            String message = String.format("Attempting to add data at index %d to SinglyLinkedList of size %d", index, size);
            throw new IndexOutOfBoundsException(message);
        }
        if (index == 0) {
            addToFront(data);
        } else if (size > 1 && index == size) {
            addToBack(data);
        } else {
            LinkedListNode<T> prev = getPrevNode(index);
            prev.setNext(new LinkedListNode<>(data, prev.getNext()));
            size++;
        }
    }

    /**
     * Returns LinkedListNode that points to Node at index
     * calling function must guarantee 1 <= index < size
     *
     * @param index the requested index for the new element
     * @return the Node pointing to Node at index
     */
    private LinkedListNode<T> getPrevNode(int index) {
        LinkedListNode<T> prev = head;
        for (int i = 0; i < index-1; i++) {
            prev = prev.getNext();
        }
        return prev;
    }

    /**
     * Adds non-null to empty list. Inserted node must point to itself.
     *
     * The caller must validate data before calling addToEmptyList
     *
     * @param data the data for the new element
     */
    private void addToEmptyList(T data) {
        head = new LinkedListNode<>(data);
        head.setNext(head);
        size++;
    }

    /**
     * Adds the element to the front of the list.
     *
     * Must be O(1) for all cases.
     *
     * @param data the data for the new element
     * @throws IllegalArgumentException if data is null
     */
    public void addToFront(T data) {
        if (size == 0) {
            addToEmptyList(data);
            return;
        }
        addAtIndex(1, data);
        swapData(head, head.getNext());
    }

    /**
     * Adds the element to the back of the list.
     *
     * Must be O(1) for all cases.
     *
     * @param data the data for the new element
     * @throws IllegalArgumentException if data is null
     */
    public void addToBack(T data) {
        if (size == 0) {
            addToEmptyList(data);
            return;
        }
        addAtIndex(1, data);
        swapData(head, head.getNext());
        head = head.getNext();
    }

    /**
     * Swaps the data between to LinkedListNodes
     *
     * @param node1 the first node with data to be swapped
     * @param node2 the second node with data to be swapped
     */
    private void swapData(LinkedListNode<T> node1, LinkedListNode<T> node2) {
        T tmp = node1.getData();
        node1.setData(node2.getData());
        node2.setData(tmp);
    }

    /**
     * Removes and returns the element from the index specified.
     *
     * Removing from index 0 should be O(1), all other cases are O(n).
     *
     * @param index the requested index to be removed
     * @return the data formerly located at index
     * @throws IndexOutOfBoundsException if index is negative or
     * index >= size
     */
    public T removeAtIndex(int index) {
        if (index < 0 || index >= size) {
            String message = String.format("Attempting to remove data at index %d to SinglyLinkedList of size %d", index, size);
            throw new IndexOutOfBoundsException(message);
        }
        if (index == 0 || size == 1) {
            return removeFromFront();
        }
        LinkedListNode<T> prev = getPrevNode(index);
        LinkedListNode<T> removed = prev.getNext();
        prev.setNext(prev.getNext().getNext());
        size--;
        return removed.getData();
    }

    /**
     * Removes and returns the element at the front of the list. If the list is
     * empty, return {@code null}.
     *
     * Must be O(1) for all cases.
     *
     * @return the data formerly located at the front, null if empty list
     */
    public T removeFromFront() {
        if (size == 1) {
            return removeFromSingletonList();
        }
        swapData(head, head.getNext());
        return removeAtIndex(1);
    }

    /**
     * Removes and returns the element at the back of the list. If the list is
     * empty, return {@code null}.
     *
     * Must be O(n) for all cases.
     *
     * @return the data formerly located at the back, null if empty list
     */
    public T removeFromBack() {
        return removeAtIndex(size-1);
    }

    /**
     * Removes the remaining LinkedListNode from a singleton list (size == 1)
     *
     * @return the data formerly located at the removed node
     */
    private T removeFromSingletonList() {
        T removed = head.getData();
        clear();
        return removed;
    }

    /**
     * Removes the last copy of the given data from the list.
     *
     * Must be O(n) for all cases.
     *
     * @param data the data to be removed from the list
     * @return the removed data occurrence from the list itself (not the data
     * passed in), null if no occurrence
     * @throws IllegalArgumentException if data is null
     */
    public T removeLastOccurrence(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Attempting to remove last occurrence of null data from SinglyLinkedList");
        }
        int last = -1;
        LinkedListNode<T> curr = head;
        for (int i = 0; i < size; i++) {
            if (curr.getData().equals(data)) {
               last = i; 
            }
            curr = curr.getNext();
        }
        if (last == -1) {
            return null;
        }
        return removeAtIndex(last);
    }

    /**
     * Returns the element at the specified index.
     *
     * Getting index 0 should be O(1), all other cases are O(n).
     *
     * @param index the index of the requested element
     * @return the object stored at index
     * @throws IndexOutOfBoundsException if index < 0 or
     * index >= size
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            String message = String.format("Attempting to get data at index %d from SinglyLinkedList of size %d", index, size);
            throw new IndexOutOfBoundsException(message);
        }
        LinkedListNode<T> target = index == 0 ? head : getPrevNode(index).getNext();
        return target.getData();
    }

    /**
     * Returns an array representation of the linked list.
     *
     * Must be O(n) for all cases.
     *
     * @return an array of length {@code size} holding all of the objects in
     * this list in the same order
     */
    public Object[] toArray() {
        Object[] elements = new Object[size];
        LinkedListNode<T> curr = head;
        for (int i = 0; i < size; i++) {
            elements[i] = curr.getData();
            curr = curr.getNext();
        }
        return elements;
    }

    /**
     * Returns a boolean value indicating if the list is empty.
     *
     * Must be O(1) for all cases.
     *
     * @return true if empty; false otherwise
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Clears the list of all data.
     *
     * Must be O(1) for all cases.
     */
    public void clear() {
        head = null;
        size = 0;
    }

    /**
     * Returns the number of elements in the list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the size of the list
     */
    public int size() {
        // DO NOT MODIFY!
        return size;
    }

    /**
     * Returns the head node of the linked list.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return node at the head of the linked list
     */
    public LinkedListNode<T> getHead() {
        // DO NOT MODIFY!
        return head;
    }
}
