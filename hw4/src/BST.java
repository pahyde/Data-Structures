import java.util.Collection;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Queue;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Your implementation of a binary search tree.
 *
 * @author Parker Hyde
 * @version 1.0
 */
public class BST<T extends Comparable<? super T>> {
    // DO NOT ADD OR MODIFY INSTANCE VARIABLES.
    private BSTNode<T> root;
    private int size;

    /**
     * A no-argument constructor that should initialize an empty BST.
     *
     * Since instance variables are initialized to their default values, there
     * is no need to do anything for this constructor.
     */
    public BST() {
        // DO NOT IMPLEMENT THIS CONSTRUCTOR!
    }

    /**
     * Initializes the BST with the data in the Collection. The data
     * should be added in the same order it is in the Collection.
     *
     * Hint: Not all Collections are indexable like Lists, so a regular for loop
     * will not work here. However, all Collections are Iterable, so what type
     * of loop would work?
     *
     * @param data the data to add to the tree
     * @throws IllegalArgumentException if data or any element in data is null
     */
    public BST(Collection<T> collection) {
        if (collection == null) {
            throw new IllegalArgumentException("Attempting to initialize BST with null collection");
        }
        try {
            for (T data : collection) {
                add(data);
            }
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException("Attempting to initialize BST with collection containing null data");
        }
    }

    /**
     * Add the data as a leaf in the BST. Should traverse the tree to find the
     * appropriate location. If the data is already in the tree, then nothing
     * should be done (the duplicate shouldn't get added, and size should not be
     * incremented).
     * 
     * Should have a running time of O(log n) for a balanced tree, and a worst
     * case of O(n).
     *
     * @throws IllegalArgumentException if the data is null
     * @param data the data to be added
     */
    public void add(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Attempting to add null data to BST");
        }
        if (size == 0) {
            root = new BSTNode<>(data);
            size = 1;
            return;
        }
        BSTNode<T> curr = root;
        while (true) {
            if (data.compareTo(curr.getData()) > 0) {
                if (curr.getRight() == null) {
                    curr.setRight(new BSTNode<>(data));
                    size++;
                    return;
                }
                curr = curr.getRight();
            } else if (data.compareTo(curr.getData()) < 0) {
                if (curr.getLeft() == null) {
                    curr.setLeft(new BSTNode<>(data));
                    size++;
                    return;
                }
                curr = curr.getLeft();
            } else {
                return;
            }
        }
    }


    /**
     * Removes the data from the tree. There are 3 cases to consider:
     *
     * 1: the data is a leaf (no children). In this case, simply remove it.
     * 2: the data has one child. In this case, simply replace it with its
     * child.
     * 3: the data has 2 children. Use the predecessor to replace the data.
     * You MUST use recursion to find and remove the predecessor (you will
     * likely need an additional helper method to handle this case efficiently).
     *
     * Should have a running time of O(log n) for a balanced tree, and a worst
     * case of O(n).
     *
     * @throws IllegalArgumentException if the data is null
     * @throws java.util.NoSuchElementException if the data is not found
     * @param data the data to remove from the tree.
     * @return the data removed from the tree. Do not return the same data
     * that was passed in. Return the data that was stored in the tree.
     */
    public T remove(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Attempting to remove null data from BST");
        }
        BSTNode<T> parent = null;
        BSTNode<T> curr = root;
        boolean isLeftChild = false;
        while (curr != null) {
            T currData = curr.getData();
            if (data.compareTo(currData) < 0) {
                isLeftChild = true;
                parent = curr;
                curr = curr.getLeft();
            } else if (data.compareTo(currData) > 0) {
                isLeftChild = false;
                parent = curr;
                curr = curr.getRight();
            } else {
                return removeNode(curr, parent, isLeftChild);
            }
        }
        throw new NoSuchElementException("Attempting to remove data not present in BST");
    }


    /**
     * Helper method to remove a target node from the BST
     *
     * @param target the node to be deleted
     * @param parent parent of the node to be deleted
     * @param isLeftChild boolean denoting whether curr is the left child of parent
     */
    private T removeNode(BSTNode<T> target, BSTNode<T> parent, boolean isLeftChild) {
        BSTNode<T> left = target.getLeft();
        BSTNode<T> right = target.getRight();
        T removed = target.getData();
        if (left != null && right != null) {
            // two child target
            replaceWithPredecessor(target);
        } else if (left != null) {
            // single left child target
            replaceWithChild(target, parent, left, isLeftChild);
        } else {
            // single right child target or leaf target
            replaceWithChild(target, parent, right, isLeftChild);
        }
        size--;
        return removed;
    }

    /**
     * Helper method to remove a 1-child or leaf node from the BST
     *
     * @param target the target node to be removed
     * @param parent parent of the node to be deleted
     * @param isLeftChild boolean denoting whether curr is the left child of parent
     */
    private void replaceWithChild(BSTNode<T> target, BSTNode<T> parent, BSTNode<T> child, boolean isLeftChild) {
        if (isLeftChild) {
            parent.setLeft(child);
        } else {
            parent.setRight(child);
        }
    }

    /**
     * Helper method to remove a 2-child node from the BST
     *
     * @param target the target node to be removed
     */
    private void replaceWithPredecessor(BSTNode<T> target) {
        if (target.getLeft().getRight() == null) {
            target.setData(target.getLeft().getData());
            target.setLeft(null);
        }
        // pparent - predecessor parent node
        // pparent
        //       \
        //        predecessor
        BSTNode<T> pparent = target.getLeft();
        while (pparent.getRight().getRight() != null) {
            pparent = pparent.getRight();
        }
        target.setData(pparent.getRight().getData());
        pparent.setRight(null);
    }
    /**
     * Returns the data in the tree matching the parameter passed in (think
     * carefully: should you use value equality or reference equality?).
     *
     * Should have a running time of O(log n) for a balanced tree, and a worst
     * case of O(n).
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
            throw new IllegalArgumentException("Attempting to get null data from BST");
        }
        BSTNode<T> found = findNode(data);
        if (found == null) {
            throw new NoSuchElementException("Attempting to get data not present in the BST");
        }
        return found.getData();
    }


    /**
     * Helper method to find the node containing the input data
     *
     * @param data the data corresponding to the target node in the BST
     * @return Node containing the matching data. Returns null if no such node exists
     */
    private BSTNode<T> findNode(T data) {
        BSTNode<T> curr = root;
        while (curr != null) {
            T currData = curr.getData();
            if (data.compareTo(currData) > 0) {
                curr = curr.getRight();
            } else if (data.compareTo(currData) < 0) {
                curr = curr.getLeft();
            } else {
                return curr;
            }
        }
        return null;
    }

    /**
     * Returns whether or not data equivalent to the given parameter is
     * contained within the tree. The same type of equality should be used as
     * in the get method.
     *
     * Should have a running time of O(log n) for a balanced tree, and a worst
     * case of O(n).
     *
     * @throws IllegalArgumentException if the data is null
     * @param data the data to search for in the tree.
     * @return whether or not the parameter is contained within the tree.
     */
    public boolean contains(T data) {
        if (data == null) {
            throw new IllegalArgumentException("Attempting to check if BST contains null data");
        }
        return findNode(data) != null;
    }

    /**
     * Should run in O(n).
     *
     * @return a preorder traversal of the tree
     */
    public List<T> preorder() {
        List<T> res = new ArrayList<>();
        preorderHelper(res, root);
        return res;
    }

    /**
     * preorder helper method.
     *
     * @param traversal a running list of the traversed nodes
     */
    private void preorderHelper(List<T> traversal, BSTNode<T> curr) {
        if (curr == null) {
            return;
        }
        traversal.add(curr.getData());
        preorderHelper(traversal, curr.getLeft());
        preorderHelper(traversal, curr.getRight());
    }


    /**
     * Should run in O(n).
     *
     * @return an inorder traversal of the tree
     */
    public List<T> inorder() {
        List<T> res = new ArrayList<>();
        inorderHelper(res, root);
        return res;
    }

    /**
     * inorder helper method.
     *
     * @param traversal a running list of the traversed nodes
     */
    private void inorderHelper(List<T> traversal, BSTNode<T> curr) {
        if (curr == null) {
            return;
        }
        inorderHelper(traversal, curr.getLeft());
        traversal.add(curr.getData());
        inorderHelper(traversal, curr.getRight());
    }

    /**
     * Should run in O(n).
     *
     * @return a postorder traversal of the tree
     */
    public List<T> postorder() {
        List<T> res = new ArrayList<>();
        postorderHelper(res, root);
        return res;
    }

    /**
     * postorder helper method.
     *
     * @param traversal a running list of the traversed nodes
     */
    private void postorderHelper(List<T> traversal, BSTNode<T> curr) {
        if (curr == null) {
            return;
        }
        postorderHelper(traversal, curr.getLeft());
        postorderHelper(traversal, curr.getRight());
        traversal.add(curr.getData());
    }

    /**
     * Generate a level-order traversal of the tree.
     *
     * To do this, add the root node to a queue. Then, while the queue isn't
     * empty, remove one node, add its data to the list being returned, and add
     * its left and right child nodes to the queue. If what you just removed is
     * {@code null}, ignore it and continue with the rest of the nodes.
     *
     * Should run in O(n). This does not need to be done recursively.
     *
     * @return a level order traversal of the tree
     */
    public List<T> levelorder() {
        List<T> res = new ArrayList<>();
        Queue<BSTNode<T>> queue = new LinkedList<>();
        queue.add(root);
        while (!queue.isEmpty()) {
            BSTNode<T> next = queue.remove();
            if (next == null) {
                continue;
            }
            res.add(next.getData());
            queue.add(next.getLeft());
            queue.add(next.getRight());
        }
        return res;
    }

    /**
     * This method checks whether a binary tree meets the criteria for being
     * a binary search tree.
     *
     * This method is a static method that takes in a BSTNode called
     * {@code treeRoot}, which is the root of the tree that you should check.
     *
     * You may assume that the tree passed in is a proper binary tree; that is,
     * there are no loops in the tree, the parent-child relationship is
     * correct, that there are no duplicates, and that every parent has at
     * most 2 children. So, what you will have to check is that the order
     * property of a BST is still satisfied.
     *
     * Should run in O(n). However, you should stop the check as soon as you
     * find evidence that the tree is not a BST rather than checking the rest
     * of the tree.
     *
     * @param <T> the generic typing
     * @param treeRoot the root of the binary tree to check
     * @return true if the binary tree is a BST, false otherwise
     */
    public static <T extends Comparable<? super T>> boolean isBST(BSTNode<T> treeRoot) {
        if (treeRoot == null) {
            return true;
        }
        BSTNode<T> left = treeRoot.getLeft();
        BSTNode<T> right = treeRoot.getRight();
        if (left != null && (treeRoot.getData().compareTo(left.getData()) <= 0 || !isBST(left))) {
            return false;
        }
        if (right != null && (treeRoot.getData().compareTo(right.getData()) >= 0 || !isBST(right))) {
            return false;
        }
        return true;
    }


    /**
     * Clears the tree.
     *
     * Should run in O(1).
     */
    public void clear() {
        root = null;
        size = 0;
    }

    /**
     * Calculate and return the height of the root of the tree. A node's
     * height is defined as {@code max(left.height, right.height) + 1}. A leaf
     * node has a height of 0 and a null child should be -1.
     *
     * Should be calculated in O(n).
     *
     * @return the height of the root of the tree, -1 if the tree is empty
     */
    public int height() {
        return nodeHeight(root);
    }

    /**
     * height helper method.
     *
     * @param curr the node with height to be calculated
     * @return the height of the given node
     */
    private int nodeHeight(BSTNode<T> curr) {
        if (curr == null) {
            return -1;
        }
        return 1 + Math.max(nodeHeight(curr.getLeft()), nodeHeight(curr.getRight()));
    }

    /**
     * Returns the size of the BST.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the number of elements in the tree
     */
    public int size() {
        // DO NOT MODIFY THIS METHOD!
        return size;
    }

    /**
     * Returns the root of the BST.
     *
     * For grading purposes only. You shouldn't need to use this method since
     * you have direct access to the variable.
     *
     * @return the root of the tree
     */
    public BSTNode<T> getRoot() {
        // DO NOT MODIFY THIS METHOD!
        return root;
    }
}
