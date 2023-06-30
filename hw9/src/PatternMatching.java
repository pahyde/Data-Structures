import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Your implementations of various string searching algorithms.
 *
 * @author Parker Hyde
 * @version 1.0
 */
public class PatternMatching {

    /**
     * Knuth-Morris-Pratt (KMP) algorithm that relies on the failure table (also
     * called failure function). Works better with small alphabets.
     *
     * Make sure to implement the failure table before implementing this method.
     *
     * @throws IllegalArgumentException if the pattern is null or of length 0
     * @throws IllegalArgumentException if text or comparator is null
     * @param pattern the pattern you are searching for in a body of text
     * @param text the body of text where you search for pattern
     * @param comparator you MUST use this for checking character equality
     * @return list containing the starting index for each match found
     */
    public static List<Integer> kmp(CharSequence pattern, CharSequence text, CharacterComparator comparator) {
        checkArgs(pattern, text, comparator);
        List<Integer> matches = new ArrayList<>();
        if (pattern.length() > text.length()) {
            return matches;
        }
        int[] failureTable = buildFailureTable(pattern, comparator);
        int patternLength = pattern.length();
        int i = 0;
        int j = 0;
        while (i + (patternLength-j) <= text.length()) {
            if (comparator.compare(text.charAt(i), pattern.charAt(j)) == 0) {
                i++;
                j++;
            } else if (j == 0) {
                i++;
            } else {
                j = failureTable[j-1];
            }
            if (j == patternLength) {
                matches.add(i - patternLength);
                j = failureTable[j-1];
            }
        }
        return matches;
    }

    /**
     * Builds failure table that will be used to run the Knuth-Morris-Pratt
     * (KMP) algorithm.
     *
     * The table built should be the length of the input text.
     *
     * Note that a given index i will be the largest prefix of the pattern
     * indices [0..i] that is also a suffix of the pattern indices [1..i].
     * This means that index 0 of the returned table will always be equal to 0
     *
     * Ex. ababac
     *
     * table[0] = 0
     * table[1] = 0
     * table[2] = 1
     * table[3] = 2
     * table[4] = 3
     * table[5] = 0
     *
     * If the pattern is empty, return an empty array.
     *
     * @throws IllegalArgumentException if the pattern or comparator is null
     * @param pattern a {@code CharSequence} you're building a failure table for
     * @param comparator you MUST use this for checking character equality
     * @return integer array holding your failure table
     */
    public static int[] buildFailureTable(CharSequence pattern, CharacterComparator comparator) {
        checkNullArg(pattern, "Attempting to build failure table with null pattern");
        checkNullArg(pattern, "Attempting to build failure table with null comparator");
        if (pattern.length() == 0) {
            return new int[0];
        }
        int[] table = new int[pattern.length()];
        table[0] = 0;
        int i = 0;
        int j = 1;
        while (j < pattern.length()) {
            if (comparator.compare(pattern.charAt(i), pattern.charAt(j)) == 0) {
                table[j] = i+1;
                i++;
                j++;
            } else if (i > 0) {
                i = table[i-1];
            } else {
                //table[j] = 0;
                table[j] = i;
                j++;
            }
        }
        return table;
    }

    /**
     * Boyer Moore algorithm that relies on last occurrence table. Works better
     * with large alphabets.
     *
     * Make sure to implement the last occurrence table before implementing this
     * method.
     *
     * Note: You may find the getOrDefault() method useful from Java's Map.
     *
     * @throws IllegalArgumentException if the pattern is null or of length 0
     * @throws IllegalArgumentException if text or comparator is null
     * @param pattern the pattern you are searching for in a body of text
     * @param text the body of text where you search for the pattern
     * @param comparator you MUST use this for checking character equality
     * @return list containing the starting index for each match found
     */
    public static List<Integer> boyerMoore(CharSequence pattern, CharSequence text, CharacterComparator comparator) {
        checkArgs(pattern, text, comparator);
        List<Integer> matches = new ArrayList<>();
        if (pattern.length() > text.length()) {
            return matches;
        }
        Map<Character, Integer> table = buildLastTable(pattern);
        int patternLength = pattern.length();
        int textLength = text.length();
        int i = 0;
        int offset = patternLength - 1;
        while (i + patternLength <= textLength) {
            if (comparator.compare(text.charAt(i + offset), pattern.charAt(offset)) != 0) {
                // mismatch char
                int lastIndex = table.getOrDefault(text.charAt(i+offset), -1);
                i += Math.max(1, offset - lastIndex);
                offset = patternLength-1;
                continue;
            }
            // match char
            offset--;
            if (offset == -1) {
                // match pattern
                matches.add(i);
                i++;
                offset = patternLength-1;
            }
        }
        return matches;
    }

    /**
     * Builds last occurrence table that will be used to run the Boyer Moore
     * algorithm.
     *
     * Note that each char x will have an entry at table.get(x).
     * Each entry should be the last index of x where x is a particular
     * character in your pattern.
     * If x is not in the pattern, then the table will not contain the key x,
     * and you will have to check for that in your Boyer Moore implementation.
     *
     * Ex. octocat
     *
     * table.get(o) = 3
     * table.get(c) = 4
     * table.get(t) = 6
     * table.get(a) = 5
     * table.get(everything else) = null, which you will interpret in
     * Boyer-Moore as -1
     *
     * If the pattern is empty, return an empty map.
     *
     * @throws IllegalArgumentException if the pattern is null
     * @param pattern a {@code CharSequence} you are building last table for
     * @return a Map with keys of all of the characters in the pattern mapping
     *         to their last occurrence in the pattern
     */
    public static Map<Character, Integer> buildLastTable(CharSequence pattern) {
        if (pattern == null) {
            throw new IllegalArgumentException("Attempting to build last table with null pattern");
        }
        Map<Character, Integer> table = new HashMap<>();
        for (int i = 0; i < pattern.length(); i++) {
            char c = pattern.charAt(i);
            table.put(c, i);
        }
        return table;
    }

    /**
     * Prime base used for Rabin-Karp hashing.
     * DO NOT EDIT!
     */
    private static final int BASE = 101;

    /**
     * Runs the Rabin-Karp algorithm. This algorithms generates hashes for the
     * pattern and compares this hash to substrings of the text before doing
     * character by character comparisons.
     *
     * When the hashes are equal and you do character comparisons, compare
     * starting from the beginning of the pattern to the end, not from the end
     * to the beginning.
     *
     * You must use the Rabin-Karp Rolling Hash for this implementation. The
     * formula for it is:
     *
     * sum of: c * BASE ^ (pattern.length - 1 - i), where c is the integer
     * value of the current character, and i is the index of the character
     *
     * Note that if you were dealing with very large numbers here, your hash
     * will likely overflow. However, you will not need to handle this case.
     * You may assume there will be no overflow.
     *
     * For example: Hashing "bunn" as a substring of "bunny" with base 101 hash
     * = b * 101 ^ 3 + u * 101 ^ 2 + n * 101 ^ 1 + n * 101 ^ 0 = 98 * 101 ^ 3 +
     * 117 * 101 ^ 2 + 110 * 101 ^ 1 + 110 * 101 ^ 0 = 102174235
     *
     * Another key step for this algorithm is that updating the hashcode from
     * one substring to the next one must be O(1). To update the hash:
     *
     * remove the oldChar times BASE raised to the length - 1, multiply by
     * BASE, and add the newChar.
     *
     * For example: Shifting from "bunn" to "unny" in "bunny" with base 101
     * hash("unny") = (hash("bunn") - b * 101 ^ 3) * 101 + y =
     * (102174235 - 98 * 101 ^ 3) * 101 + 121 = 121678558
     *
     * Keep in mind that calculating exponents is not O(1) in general, so you'll
     * need to keep track of what BASE^{m - 1} is for updating the hash.
     *
     * Do NOT use Math.pow() for this method.
     *
     * @throws IllegalArgumentException if the pattern is null or of length 0
     * @throws IllegalArgumentException if text or comparator is null
     * @param pattern a string you're searching for in a body of text
     * @param text the body of text where you search for pattern
     * @param comparator the comparator to use when checking character equality
     * @return list containing the starting index for each match found
     */
    public static List<Integer> rabinKarp(CharSequence pattern, CharSequence text, CharacterComparator comparator) {
        checkArgs(pattern, text, comparator);
        List<Integer> matches = new ArrayList<>();
        if (pattern.length() > text.length()) {
            return matches;
        }
        // compute pattern hash
        int patternHash = 0;
        for (int i = 0; i < pattern.length(); i++) {
            patternHash *= BASE;
            patternHash += pattern.charAt(i);
        }
        // compute initial text substring hash and maxCoeff (i.e. BASE^{m-1})
        // initial text hash corresponds to pattern[-1: pattern.length()-1]
        // where the first char: pattern[-1] is interpreted as 0 for convenience
        int maxCoeff = 1;
        int textHash = 0;
        for (int i = 0; i < pattern.length()-1; i++) {
            maxCoeff *= BASE;
            textHash *= BASE;
            textHash += text.charAt(i);
        }
        char first = 0;
        for (int i = pattern.length()-1; i < text.length(); i++) {
            textHash -= first * maxCoeff;
            textHash *= BASE;
            textHash += text.charAt(i);
            int firstIndex = i - pattern.length() + 1;
            if (textHash == patternHash) {
                if (charsMatch(pattern, text, firstIndex, pattern.length(), comparator)) {
                    matches.add(firstIndex);
                }
            }
            first = text.charAt(firstIndex);
        }
        return matches;
    }

    /*
     * Helper method for rabinKarp
     * Verifies that hash matches correspond to character-to-character
     * correspondence between give pattern and text substring
     *
     * @param pattern the pattern to match
     * @param text the text containing a substring with hash matching pattern hash
     * @param start the start index of the substring in text
     * @param length the length of the pattern
     * @param comparator the character comparator
     * @return true if pattern chars match text chars, else false
     */
    private static boolean charsMatch(
        CharSequence pattern, 
        CharSequence text, 
        int start, 
        int length,
        CharacterComparator comparator
    ) {
        for (int i = 0; i < length; i++) {
            char c1 = pattern.charAt(i);
            char c2 = text.charAt(start + i);
            if (comparator.compare(c1, c2) != 0) {
                return false;
            }
        }
        return true;
    }

    /*
     * Helper method to check illegal argument values
     * @throws IllegalArgumentException if the pattern is null or of length 0
     * @throws IllegalArgumentException if text or comparator is null
     * @param pattern a string you're searching for in a body of text
     * @param text the body of text where you search for pattern
     * @param comparator the comparator to use when checking character equality
     */
    private static void checkArgs(CharSequence pattern, CharSequence text, CharacterComparator comparator) {
        checkNullArg(pattern, "Attempting to search for null pattern in text");
        checkNullArg(text, "Attempting to search for pattern in null text. text cannot be null");
        checkNullArg(comparator, "Attempting to search using null comparator");
        if (pattern.length() == 0) {
            throw new IllegalArgumentException("Cannot search for pattern of length 0 in text");
        }
    }

    /*
     * checkArgs helper method
     * @throws IllegalArgumentException if arg is null
     * @param arg the arg inspect
     */
    private static void checkNullArg(Object arg, String exceptionMessage) {
        if (arg == null) {
            throw new IllegalArgumentException(exceptionMessage);
        }
    }
}
