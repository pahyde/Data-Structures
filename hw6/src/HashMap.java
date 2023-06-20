import java.util.HashSet;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Set;
import java.util.ArrayList;

/**
 * Your implementation of HashMap.
 *
 * @author Parker Hyde
 * @version 1.0
 */
public class HashMap<K, V> {

    // DO NOT MODIFY OR ADD NEW GLOBAL/INSTANCE VARIABLES
    public static final int INITIAL_CAPACITY = 11;
    public static final double MAX_LOAD_FACTOR = 0.67;
    private MapEntry<K, V>[] table;
    private int size;

    /**
     * Creates a hash map with no entries. The backing array should have an
     * initial capacity of INITIAL_CAPACITY.
     *
     * Use constructor chaining.
     */
    public HashMap() {
        this(INITIAL_CAPACITY);
    }

    /**
     * Creates a hash map with no entries. The backing array should have an
     * initial capacity of the initialCapacity parameter.
     *
     * You may assume the initialCapacity parameter will always be positive.
     *
     * @param initialCapacity initial capacity of the backing array
     */
    public HashMap(int initialCapacity) {
        table = new MapEntry[initialCapacity];
    }

    /**
     * Adds the given key-value pair to the HashMap.
     *
     * In the case of a collision, use external chaining as your resolution
     * strategy. Add new entries to the front of an existing chain, but don't
     * forget to check the entire chain for duplicate keys first.

     * If you find a duplicate key, then replace the entry's value with the new
     * one passed in. When replacing the old value, replace it at that position
     * in the chain, not by creating a new entry and adding it to the front.
     *
     * At the start of the method, you should check to see if the array would
     * violate the max load factor after adding the data (regardless of
     * duplicates). For example, let's say the array is of length 5 and the
     * current size is 3 (LF = 0.6). For this example, assume that no elements
     * are removed in between steps. If another entry is attempted to be added,
     * before doing anything else, you should check whether (3 + 1) / 5 = 0.8
     * is larger than the max LF. It is, so you would trigger a resize before
     * you even attempt to add the data or figure out if it's a duplicate. As a
     * warning, be careful about using integer division in the LF calculation!
     *
     * When regrowing, resize the length of the backing table to
     * 2 * old length + 1. You must use the resizeBackingTable method to do so.
     *
     * Return null if the key was not already in the map. If it was in the map,
     * return the old value associated with it.
     *
     * @param key key to add into the HashMap
     * @param value value to add into the HashMap
     * @throws IllegalArgumentException if key or value is null
     * @return null if the key was not already in the map. If it was in the
     * map, return the old value associated with it
     */
    public V put(K key, V value) {
        if (key == null) {
            throw new IllegalArgumentException("Attempting to insert null key into HashMap");
        } else if (value == null) {
            throw new IllegalArgumentException("Attempting to insert null value into HashMap");
        }
        if (size+1 > MAX_LOAD_FACTOR * table.length) {
            resizeBackingTable(2*table.length + 1);
        }
        int bucket = getBucket(key);
        MapEntry<K, V> entry = getEntry(bucket, key);
        if (entry != null) {
            V prev = entry.getValue();
            entry.setValue(value);
            return prev;
        }
        table[bucket] = new MapEntry<>(key, value, table[bucket]);
        size++;
        return null;
    }

    /**
     * Resizes the backing table to the specified length.
     *
     * Disregard the load factor for this method. So, if the passed in length is
     * smaller than the current capacity, and this new length causes the table's
     * load factor to exceed MAX_LOAD_FACTOR, you should still resize the table
     * to the specified length and leave it at that capacity.
     *
     * You should iterate over the old table in order of increasing index, and
     * iterate over each chain from front to back. Add entries to the new table
     * in the order in which they are traversed.
     *
     * Remember, you cannot simply copy the entries over to the new array.
     * You will have to rehash all of the entries and add them to the new index
     * of the new table. Feel free to create new MapEntry objects to use when
     * adding to the new table to avoid pointer dependency issues between the
     * new and old tables.
     *
     * Also, since resizing the backing table is working with the non-duplicate
     * data already in the table, you shouldn't explicitly check for
     * duplicates. This matters especially for external chaining since it can
     * cause the performance of resizing to go from linear to quadratic time.
     *
     * @param length new length of the backing table
     * @throws IllegalArgumentException if length is non-positive or less than
     * the number of items in the hash map.
     */
    public void resizeBackingTable(int length) {
        if (length == 0) {
            throw new IllegalArgumentException("Attempting to resize backing table to length 0");
        }
        if (length < size) {
            String message = String.format("Attempting to resize backing table of size %d to length %d", size, length);
            throw new IllegalArgumentException(message);
        }
        MapEntry<K, V>[] newTable = new MapEntry[length];
        for (int i = 0; i < table.length; i++) {
            for (MapEntry<K, V> curr = table[i]; curr != null; curr = curr.getNext()) {
                K key = curr.getKey();
                V value = curr.getValue();
                int bucket = getBucket(key);
                newTable[bucket] = new MapEntry<>(key, value, newTable[bucket]);
            }
        }
        table = newTable;
    }

    /**
     * Removes the entry with a matching key from the HashMap.
     *
     * @param key the key to remove
     * @throws IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException if the key does not exist
     * @return the value previously associated with the key
     */
    public V remove(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Attempting to remove key not present in HashMap");
        }
        int bucket = getBucket(key);
        MapEntry<K, V> prev = null;
        MapEntry<K, V> curr = table[bucket];
        while (curr != null) {
            if (!curr.getKey().equals(key)) {
                prev = curr;
                curr = curr.getNext();
                continue;
            }
            V removed = curr.getValue();
            if (prev == null) {
                table[bucket] = curr.getNext();
            } else {
                prev.setNext(curr.getNext());
            }
            return removed;
        }
        throw new NoSuchElementException("Cannot remove: provided key is not present in HashMap");
    }

    /**
     * Gets the value associated with the given key.
     *
     * @param key the key to search for
     * @throws IllegalArgumentException if key is null
     * @throws java.util.NoSuchElementException if the key is not in the map
     * @return the value associated with the given key
     */
    public V get(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Attempting to get value for null key");
        }
        int bucket = getBucket(key);
        MapEntry<K, V> entry = getEntry(bucket, key);
        if (entry == null) {
            throw new NoSuchElementException("Attempting to get value from a key not present in the HashMap");
        }
        return entry.getValue();
    }

    /**
     * Returns whether or not the key is in the map.
     *
     * @param key the key to search for
     * @throws IllegalArgumentException if key is null
     * @return whether or not the key is in the map
     */
    public boolean containsKey(K key) {
        if (key == null) {
            throw new IllegalArgumentException("Attempting to check if HashMap contains null key");
        }
        return getEntry(getBucket(key), key) != null;
    }

    /**
     * Returns a Set view of the keys contained in this map. The Set view is
     * used instead of a List view because keys are unique in a HashMap, which
     * is a property that elements of Sets also share.
     *
     * Use java.util.HashSet.
     *
     * @return set of keys in this map
     */
    public Set<K> keySet() {
        Set<K> keys = new HashSet<>();
        for (int i = 0; i < table.length; i++) {
            for (MapEntry<K, V> curr = table[i]; curr != null; curr = curr.getNext()) {
                keys.add(curr.getKey());
            }
        }
        return keys;
    }

    /**
     * Returns a List view of the values contained in this map.
     *
     * Use java.util.ArrayList or java.util.LinkedList.
     *
     * You should iterate over the table in order of increasing index, and
     * iterate over each chain from front to back. Add entries to the List in
     * the order in which they are traversed.
     *
     * @return list of values in this map
     */
    public List<V> values() {
        List<V> values = new ArrayList<>();
        for (int i = 0; i < table.length; i++) {
            for (MapEntry<K, V> curr = table[i]; curr != null; curr = curr.getNext()) {
                values.add(curr.getValue());
            }
        }
        return values;
    }

    /**
     * Clears the table and resets it to a new table of length INITIAL_CAPACITY.
     */
    public void clear() {
        table = new MapEntry[INITIAL_CAPACITY];
        size = 0;
    }

    /*
     * Computes the bucket (table index) of a given key.
     * The bucket is obtained by 
     *  1) computing the hashCode of the key
     *  2) taking the remainder of hashCode / table length
     *  3) computing the absolute value of the remainder
     *
     * @param key the key required to compute the bucket
     */
    private int getBucket(K key) {
        return Math.abs(key.hashCode() % table.length);
    }

    /*
     * Returns the MapEntry associated with a given bucket and key.
     * If no such key is found in the given bucket, returns null
     *
     * @param bucket the bucket associated with the MapEntry
     * @param key the key associated with the MapEntry
     * @return MapEntry in the given bucket matching the given key
     */
    private MapEntry<K, V> getEntry(int bucket, K key) {
        for (MapEntry<K, V> curr = table[bucket]; curr != null; curr = curr.getNext()) {
            if (curr.getKey().equals(key)) {
                return curr;
            }
        }
        return null;
    }

    /**
     * Returns the size of the HashMap.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return number of items in the HashMap
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }

    /**
     * Returns the backing table of the HashMap.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the backing table of the HashMap
     */
    public MapEntry<K, V>[] getTable() {
        // DO NOT MODIFY THIS METHOD!
        return table;
    }
}
