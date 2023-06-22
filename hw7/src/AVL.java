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
            for (T item : data) {
                add(item);
            }
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
     * Remember to recalculate heights and balance factors going up the tree,
     * rebalancing if necessary.
     *
     * @throws java.lang.IllegalArgumentException if the data is null
     * @param data the data to be added
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Attempting to add null data to AVL tree");
        }
        root = addHelper(root, data);
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
    private AVLNode<T> addHelper(AVLNode<T> curr, T data) {
        if (curr == null) {
            return new AVLNode<T>(data);
        }
        int cmp = data.compareTo(curr.getData());
        if (cmp == 0) {
            // duplicate
            return curr;
        } else if (cmp < 0) {
            // add left
            curr.setLeft(addHelper(curr.getLeft(), data));
        } else if (cmp > 0) {
            // add right
            curr.setRight(addHelper(curr.getRight(), data));
        }
        updateStats(curr);
        balanceAVLNode(curr);
        return curr;
    }

    /*
     * updates the height and balanceFactor for the given node
     * O(1) time complexity
     *
     * @param target the node to update
     */
    private void updateStats(AVLNode<T> target) {
        if (target == null) {
            return;
        }
        AVLNode<T> left = target.getLeft();
        AVLNode<T> right = target.getRight();
        int leftHeight = left == null ? -1 : left.getHeight();
        int rightHeight = right == null ? -1 : right.getHeight();
        target.setHeight(1 + Math.max(leftHeight, rightHeight));
        target.setBalanceFactor(leftHeight - rightHeight);
    }

    /*
     * rebalances the given node using a strategy corresponding to
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
     *
     * @param target the node to update
     */
    private void balanceAVLNode(AVLNode<T> target) {
        int balance = target.getBalanceFactor();
        if (Math.abs(balance) <= 1) {
            return;
        }
        AVLNode<T> child = balance > 0 ? target.getLeft(): target.getRight();
        int childBalance = child.getBalanceFactor();
        if (balance > 0 && childBalance > 0){
            System.out.println("rotate Left.. Left!");
            rotateLeftLeft(
                target, 
                child,
                child.getLeft()
            );
        } else if (balance > 0 && childBalance < 0) {
            rotateLeftRight(
                target, 
                child,
                child.getRight()
            );
        } else if (balance < 0 && childBalance > 0) {
            rotateRightLeft(
                target, 
                child, 
                child.getLeft()
            );
        } else {
            rotateRightRight(
                target, 
                child, 
                child.getRight()
            );
        }
        // target is now balanced
        updateStats(target.getLeft());
        updateStats(target.getRight());
        updateStats(target);
    }

    /*
     * rotates unbalanced node for the Left-Left case
     *
     * @param target target node to rebalance
     * @param child child of target
     * @param grandchild grandchild of target
     *
     * child  - target
     *        \ gchild
     *
     * target - tRightChild
     *        \ child.right
     *
     *     target(child)
     *  c.left     child(target)
     *             c.right   target.right
     *
     */
    private void rotateLeftLeft(
        AVLNode<T> target, 
        AVLNode<T> child, 
        AVLNode<T> grandchild
    ) {
        T childData = child.getData();
        child.setData(target.getData());
        child.setLeft(child.getRight());
        child.setRight(target.getRight());
        target.setData(childData);
        target.setLeft(grandchild);
        target.setRight(child);
    }

    /*
     * rotates unbalanced node for the Left-Left case
     *
     * @param target target node to rebalance
     * @param child child of target
     * @param grandchild grandchild of target
     *
     *
     *                target(grandchild)
     *         child                   grandchild(target)
     *    c.left  gchild.left     gchild.right    target.right
     *
     */
    private void rotateLeftRight(
        AVLNode<T> target, 
        AVLNode<T> child, 
        AVLNode<T> grandchild
    ) {
        T grandchildData = grandchild.getData();
        grandchild.setData(target.getData());
        grandchild.setLeft(grandchild.getRight());
        grandchild.setRight(target.getRight());
        child.setRight(grandchild.getLeft());
        target.setData(grandchildData);
        target.setLeft(child);
        target.setRight(grandchild);
    }

    /*
     * rotates unbalanced node for the Left-Right case
     *
     * @param target target node to rebalance
     * @param child child of target
     * @param grandchild grandchild of target
     */
    private void rotateRightLeft(
        AVLNode<T> target, 
        AVLNode<T> child, 
        AVLNode<T> grandchild
    ) {
        T grandchildData = grandchild.getData();
        grandchild.setData(target.getData());
        grandchild.setRight(grandchild.getLeft());
        grandchild.setLeft(target.getLeft());
        child.setLeft(grandchild.getRight());
        target.setData(grandchildData);
        target.setRight(child);
        target.setLeft(grandchild);
    }

    /*
     * rotates unbalanced node for the Right-Right case
     *
     * @param target target node to rebalance
     * @param child child of target
     * @param grandchild grandchild of target
     */
    private void rotateRightRight(
        AVLNode<T> target, 
        AVLNode<T> child, 
        AVLNode<T> grandchild
    ) {
        T childData = child.getData();
        child.setData(target.getData());
        child.setRight(child.getLeft());
        child.setLeft(target.getLeft());
        target.setData(childData);
        target.setRight(grandchild);
        target.setLeft(child);
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
     * Remember to recalculate heights going up the tree, rebalancing if
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
        if (root == null) {
            throw new NoSuchElementException("Attempting to remove data not present in AVL tree");
        }
        if (data.equals(root.getData())) {
            T removed = root.getData();
            root = getReplacement(root);
            return removed;
        }
        try {
            T removed = removeHelper(root, data);
            size--;
            return removed;
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
     *
     */
    private T removeHelper(AVLNode<T> curr, T data) {
        int order = data.compareTo(curr.getData());
        if (order == 0) {
            throw new IllegalArgumentException("current.data should never equal data");
        }
        if (order < 0 && curr.getLeft() == null || order > 0 && curr.getRight() == null) {
            throw new NoSuchElementException("provided data not found in given subtree");
        }
        T removed = null;
        if (order < 0) {
            // search left
            if (curr.getLeft().getData().equals(data)) {
                // remove left
                removed = curr.getLeft().getData();
                AVLNode<T> replacement = getReplacement(curr.getLeft());
                curr.setLeft(replacement);
            } else {
                removed = removeHelper(curr.getLeft(), data);
            }

        } else if (order > 0) {
            // search right
            if (curr.getRight().getData().equals(data)) {
                // remove right
                removed = curr.getLeft().getData();
                AVLNode<T> replacement = getReplacement(curr.getLeft());
                curr.setLeft(replacement);
            } else {
                removed = removeHelper(curr.getRight(), data);
            }
        }
        updateStats(curr);
        balanceAVLNode(curr);
        return removed;
    } /*
     * Returns the appropriate replacement node for target
     *
     * @param target the target node to replace
     * @return the replacement node
     */
    private AVLNode<T> getReplacement(AVLNode<T> target) {
        AVLNode<T> left = target.getLeft();
        AVLNode<T> right = target.getRight();
        if (right == null) {
            return left;
        } else if (left == null) {
            return right;
        } else if (right.getLeft() == null) {
            right.setLeft(left);
            updateStats(right);
            balanceAVLNode(right);
            return right;
        }
        AVLNode<T> succ = new AVLNode<>(null);
        removeSuccessor(right, succ);
        succ.setLeft(left);
        succ.setRight(right);
        updateStats(succ);
        balanceAVLNode(succ);
        return succ;
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
    private void removeSuccessor(AVLNode<T> subtree, AVLNode<T> successor) {
        AVLNode<T> left = subtree.getLeft();
        if (left.getLeft() == null) {
            successor.setData(left.getData());
            AVLNode<T> replacement = getReplacement(left);
            subtree.setLeft(replacement);
        } else {
            removeSuccessor(left, successor);
        }
        updateStats(subtree);
        balanceAVLNode(subtree);
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
        T found = getHelper(root, data);
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
    private T getHelper(AVLNode<T> curr, T data) {
        if (curr == null) {
            return null;
        }
        int order = data.compareTo(curr.getData());
        if (order == 0) {
            return data;
        } else if (order < 0) {
            return getHelper(curr.getLeft(), data);
        } else {
            return getHelper(curr.getRight(), data);
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
        return getHelper(root, data) != null;
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
