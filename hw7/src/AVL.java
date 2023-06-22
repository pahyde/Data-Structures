import java.util.ArrayList;
import java.util.Queue;
import java.util.LinkedList;
import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.lang.StringBuilder;

/**
 * Your implementation of an AVL Tree.
 *
 * @author Parker Hyde
 * @version 1.0
 */
public class AVL<T extends Comparable<? super T>> {
    // DO NOT ADD OR MODIFY INSTANCE VARIABLES.
    private AVLNode<T> root;
    private int size;

    /**
     * A no-argument constructor that should initialize an empty AVL.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public AVL() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Initializes the AVL tree with the data in the Collection. The data
     * should be added in the same order it appears in the Collection.
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public AVL(Collection<T> data) {
        if (data == null) {
            throw new IllegalArgumentException("Attempting to initialize AVL tree with null collection");
        }
        try {
            data.forEach(this::add);
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Attempting to initialize AVL tree with collection containing null data");
        }
    }

    /**
     * Adds the data to the AVL. Start by adding it as a leaf like in a regular
     * BST and then rotate the tree as needed.
     *
     * If the data is already in the tree, then nothing should be done (the
     * duplicate shouldn't get added, and size should not be incremented).
     *
     * Remember to calculate heights and balance factors going up the tree,
     * rebalancing if necessary.
     *
     * @throws java.lang.IllegalArgumentException if the data is null
     * @param data the data to be added
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Attempting to add null data to AVL tree");
        }
        root = withData(root, data);
        size++;
    }

    /*
     * add helper method - recursively adds data to the curr node AVL tree
     * rebalances subtrees when abs(curr.balance) > 1 
     *
     * @param curr node being traversed in the tree
     * @param data the data to be added
     * @return root of balanced subtree obtained by adding data to curr subtree
     */
    private AVLNode<T> withData(AVLNode<T> curr, T data) {
        if (curr == null) {
            return new AVLNode<T>(data);
        }
        int cmp = data.compareTo(curr.getData());
        if (cmp == 0) {
            // duplicate
            return curr;
        } else if (cmp < 0) {
            // add left
            curr.setLeft(withData(curr.getLeft(), data));
        } else if (cmp > 0) {
            // add right
            curr.setRight(withData(curr.getRight(), data));
        }
        calculate(curr);
        return balance(curr);
    }

    /*
     * updates the height and balanceFactor for the given node
     * O(1) time complexity
     *
     * @param target the node to update
     */
    private void calculate(AVLNode<T> target) {
        if (target == null) {
            return;
        }
        int left = getHeight(target.getLeft());
        int right = getHeight(target.getRight());
        target.setHeight(1 + Math.max(left, right));
        target.setBalanceFactor(left - right);
    }

    /* Returns the height of a given node
     * null nodes have height -1
     *
     * @param node the given node to calculate height
     */
    private int getHeight(AVLNode<T> node) {
        return node == null ? -1 : node.getHeight();
    }

    /*
     * Checks if the given node is unbalanced
     * Then rebalances if needed using the balance helper method
     *
     * @param a the root node
     */
    private AVLNode<T> balance(AVLNode<T> target) {
        if (target == null) {
            return target;
        }
        int bf1 = target.getBalanceFactor();
        if (Math.abs(bf1) <= 1) {
            // target is already balanced
            return target;
        }
        AVLNode<T> child = bf1 > 0 ? target.getLeft() : target.getRight();
        int bf2 = child.getBalanceFactor();
        AVLNode<T> grandchild = bf2 > 0 ? child.getLeft() : child.getRight();
        return rebalance(root, child, grandchild, bf1, bf2);
    }

    /*
     * Rebalances the given node using a strategy corresponding to
     * one of the following 4 cases:
     *
     *     case 1: Left-Left 
     *         a
     *        /
     *       b
     *      /
     *     c
     *
     *     case 2: Left-Right
     *         a
     *        /
     *       b
     *        \
     *         c
     *
     *     case 3: Right-Left
     *         a
     *          \
     *           b
     *          /
     *         c
     *
     *     case 4: Right-Right
     *         a
     *          \
     *           b
     *            \
     *             c
     *
     * @param a the root node
     */
    private AVLNode<T> rebalance(AVLNode<T> a, AVLNode<T> b, AVLNode<T> c, int bf1, int bf2) {
        if (bf1 > 0 && bf2 > 0) {
            // case 1: Left-Left
            return rotateRight(a, b);
        }
        if (bf1 > 0 && bf2 < 0) {
            // case 2: Left-Right
            a.setLeft(rotateLeft(b, c));
            return rotateRight(a, c);
        }
        if (bf1 < 0 && bf2 > 0) {
            // case 3: Right-Left
            a.setRight(rotateRight(b, c));
            return rotateLeft(a, c);
        } else {
            // case 4: Righ-Right
            return rotateLeft(a, b);
        }
    }

    /*
     * rotates unbalanced nodes left
     *
     * @param parent
     * @param child 
     */
    private AVLNode<T> rotateLeft(AVLNode<T> parent, AVLNode<T> child) {
        parent.setRight(child.getLeft());
        child.setLeft(parent);
        calculate(parent);
        calculate(child);
        return child;
    }

    /*
     * rotates unbalanced nodes right
     *
     * @param parent
     * @param child 
     */
    private AVLNode<T> rotateRight(AVLNode<T> parent, AVLNode<T> child) {
        parent.setLeft(child.getRight());
        child.setRight(parent);
        calculate(parent);
        calculate(child);
        return child;
    }

    /**
     * Removes the data from the tree. There are 3 cases to consider:
     *
     * 1: the data is a leaf. In this case, simply remove it.
     * 2: the data has one child. In this case, simply replace it with its
     * child.
     * 3: the data has 2 children. Use the successor to replace the data,
     * not the predecessor. As a reminder, rotations can occur after removing
     * the successor node.
     *
     * Remember to calculate heights going up the tree, rebalancing if
     * necessary.
     *
     * @throws IllegalArgumentException if the data is null
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data the data to remove from the tree.
     * @return the data removed from the tree. Do not return the same data
     * that was passed in.  Return the data that was stored in the tree.
     *
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Attempting to remove null data from AVL tree");
        }
        try {
            AVLNode<T> removed = new AVLNode<>(null);
            root = withoutData(root, data, removed);
            size--;
            return removed.getData();
        } catch (NoSuchElementException e) {
            throw new NoSuchElementException("Attempting to remove data not present in AVL tree");
        }
    }

    /*
     * remove helper method - recursively removed data from the curr node AVL tree
     * rebalances subtrees when abs(curr.balance) > 1 
     *
     * @param curr node being traversed in the tree
     * @param data the data to be removed
     * @param removed AVLNode to store removed data
     * @return root of balanced subtree obtained after removing data from curr subtree
     */
    private AVLNode<T> withoutData(AVLNode<T> curr, T data, AVLNode<T> removed) {
        if (curr == null) {
            throw new NoSuchElementException("provided data not found in subtree");
        }
        int cmp = data.compareTo(curr.getData());
        if (cmp < 0) {
            curr.setLeft(withoutData(curr.getLeft(), data, removed));
        } else if (cmp > 0) {
            curr.setRight(withoutData(curr.getRight(), data, removed));
        } else {
            removed.setData(curr.getData());
            curr = replacement(curr);
        }
        calculate(curr);
        return balance(curr);
    } 

    /*
     * Returns the appropriate replacement node for target
     *
     * @param target the target node to replace
     * @return the replacement node
     */
    private AVLNode<T> replacement(AVLNode<T> target) {
        AVLNode<T> left = target.getLeft();
        AVLNode<T> right = target.getRight();
        if (right == null) {
            return left;
        }
        if (left == null) {
            return right;
        }
        AVLNode<T> successor = new AVLNode<>(null);
        right = withoutSuccessor(right, successor);
        successor.setLeft(left);
        successor.setRight(right);
        return successor;
    }

    /*
     * Removes the successor from the right subtree of the target
     * Reduces to finding the leftmost node
     * the removed data is then stored in the successor AVLNode
     *
     *
     * @param subtree the subtree from which to remove the successor
     * @param successor the AVLNode used to store the found successor data of type T
     * @return the subtree after removing the successor
     */
    private AVLNode<T> withoutSuccessor(AVLNode<T> curr, AVLNode<T> successor) {
        if (curr.getLeft() == null) {
            // curr is successor
            successor.setData(curr.getData());
            curr = curr.getRight();
        } else {
            curr.setLeft(withoutSuccessor(curr.getLeft(), successor));
        }
        calculate(curr);
        balance(curr);
        return curr;
    }

    /**
     * Returns the data in the tree matching the parameter passed in (think
     * carefully: should you use value equality or reference equality?).
     *
     * @throws IllegalArgumentException if the data is null
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data the data to search for in the tree.
     * @return the data in the tree equal to the parameter. Do not return the
     * same data that was passed in.  Return the data that was stored in the
     * tree.
     */
    public T get(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Attempting to get null data from AVL tree");
        }
        T found = find(root, data);
        if (found == null) {
            throw new NoSuchElementException("The provided data is not present in the AVL tree");
        }
        return found;
    }

    /* 
     * get helper method
     *
     * @param data
     * @param 
     */
    private T find(AVLNode<T> curr, T data) {
        if (curr == null) {
            return null;
        }
        int cmp = data.compareTo(curr.getData());
        if (cmp == 0) {
            return data;
        } else if (cmp < 0) {
            return find(curr.getLeft(), data);
        } else {
            return find(curr.getRight(), data);
        }
    }


    /**
     * Returns whether or not data equivalent to the given parameter is
     * contained within the tree. The same type of equality should be used as
     * in the get method.
     *
     * @throws IllegalArgumentException if the data is null
     * @param data the data to search for in the tree.
     * @return whether or not the parameter is contained within the tree.
     */
    public boolean contains(T data) {
        return find(root, data) != null;
    }

    /**
     * Returns the data on branches of the tree with the maximum depth. If you
     * encounter multiple branches of maximum depth while traversing, then you
     * should list the remaining data from the left branch first, then the
     * remaining data in the right branch. This is essentially a preorder
     * traversal of the tree, but only of the branches of maximum depth.
     *
     * Your list should not duplicate data, and the data of a branch should be
     * listed in order going from the root to the leaf of that branch.
     *
     * Should run in worst case O(n), but you should not explore branches that
     * do not have maximum depth. You should also not need to traverse branches
     * more than once.
     *
     * Hint: How can you take advantage of the balancing information stored in
     * AVL nodes to discern deep branches?
     *
     * Example Tree:
     *                           10
     *                       /        \
     *                      5          15
     *                    /   \      /    \
     *                   2     7    13    20
     *                  / \   / \     \  / \
     *                 1   4 6   8   14 17  25
     *                /           \          \
     *               0             9         30
     *
     * Returns: [10, 5, 2, 1, 0, 7, 8, 9, 15, 20, 25, 30]
     *
     * @return the list of data in branches of maximum depth in preorder
     * traversal order
     */
    public List<T> deepestBranches() {
        List<T> res = new ArrayList<>();
        deepestBranchesHelper(root, res);
        return res;
    }

    /*
     * deepestBranches Helper method
     *
     * @param curr the current node
     * @param res the resulting list of data corresponding to the deepest branches
     */
    private void deepestBranchesHelper(AVLNode<T> curr, List<T> res) {
        if (curr == null) {
            return;
        }
        res.add(curr.getData());
        int balance = curr.getBalanceFactor();
        if (balance >= 0) {
            deepestBranchesHelper(curr.getLeft(), res);
        }
        if (balance <= 0) {
            deepestBranchesHelper(curr.getRight(), res);
        }
    }

    /**
     * Returns a sorted list of data that are within the threshold bounds of
     * data1 and data2. That is, the data should be > data1 and < data2.
     *
     * Should run in worst case O(n), but this is heavily dependent on the
     * threshold data. You should not explore branches of the tree that do not
     * satisfy the threshold.
     *
     * Example Tree:
     *                           10
     *                       /        \
     *                      5          15
     *                    /   \      /    \
     *                   2     7    13    20
     *                  / \   / \     \  / \
     *                 1   4 6   8   14 17  25
     *                /           \          \
     *               0             9         30
     *
     * sortedInBetween(7, 14) returns [8, 9, 10, 13]
     * sortedInBetween(3, 8) returns [4, 5, 6, 7]
     * sortedInBetween(8, 8) returns []
     *
     * @param data1 the smaller data in the threshold
     * @param data2 the larger data in the threshold
     * @throws java.lang.IllegalArgumentException if data1 or data2 are null
     * or if data1 > data2
     * @return a sorted list of data that is > data1 and < data2
     */
    public List<T> sortedInBetween(T data1, T data2) {
        List<T> res = new ArrayList<>();
        sortedInBetweenHelper(root, data1, data2, res);
        return res;

    }

    /*
     * sortedInBetween Helper method
     *
     * @param curr the current node
     * @param res the resulting list of data corresponding to the deepest branches
     */
    private void sortedInBetweenHelper(AVLNode<T> curr, T data1, T data2, List<T> res) {
       if (curr == null) {
           return;
       } 
       int cmp1 = curr.getData().compareTo(data1);
       int cmp2 = curr.getData().compareTo(data2);
       if (cmp1 > 0) {
           sortedInBetweenHelper(curr.getLeft(), data1, data2, res);
       }
       if (cmp1 > 0 && cmp2 < 0) {
           res.add(curr.getData());
       }
       if (cmp2 < 0) {
           sortedInBetweenHelper(curr.getRight(), data1, data2, res);
       }
    }

    /**
     * Clears the tree.
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Returns the height of the root of the tree.
     *
     * Since this is an AVL, this method does not need to traverse the tree
     * and should be O(1)
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        return root.getHeight();
    }


    /**
     * Returns the size of the AVL tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return number of items in the AVL tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD
        return size;
    }
    
    /*
     * Formats AVL tree in level-order traversal
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("\n");
        Queue<AVLNode<T>> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            int levelSize = queue.size();
            for (int i = 0; i < levelSize; i++) {
                AVLNode<T> next = queue.poll();
                if (next == null) {
                    continue;
                }
                sb.append(next + " ");
                queue.add(next.getLeft());
                queue.add(next.getRight());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Returns the root of the AVL tree.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the AVL tree
     */
    public AVLNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }
}
