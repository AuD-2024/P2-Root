package p2.binarytree;

/**
 * A class for checking the rules of a red-black tree.
 */
public class RBTreeChecker {

    /**
     * Checks if the given tree satisfies all the rules of a red-black tree.
     *
     * @param rbTree the tree to check.
     * @throws RBTreeException if the tree does not satisfy any of the rules.
     */
    public static void checkAllRules(RBTree<?> rbTree) {
        checkRule1(rbTree);
        checkRule2(rbTree);
        checkRule3(rbTree);
        checkRule4(rbTree);
    }

    /**
     * Checks if the given tree satisfies the first rule of black tree.
     * <p>
     * The first rule of a red-black tree states that every node is either red or black, i.e. its color is not {@code null}.
     *
     * @param rbTree the tree to check.
     * @throws RBTreeException if the tree does not satisfy the rule.
     */
    public static void checkRule1(RBTree<?> rbTree) {
        if (rbTree.getRoot() != null) checkRule1(rbTree.getRoot());
    }

    private static void checkRule1(RBNode<?> node) {
        if (node.getColor() == null)
            throw new RBTreeException("Node with key " + node.getKey() + "does not have any color!");

        if (node.hasLeft()) checkRule1(node.getLeft());
        if (node.hasRight()) checkRule1(node.getRight());
    }

    /**
     * Checks if the given tree satisfies the second rule of black tree.
     * <p>
     * The second rule of a red-black tree states that the root of the tree is black.
     *
     * @param rbTree the tree to check.
     * @throws RBTreeException if the tree does not satisfy the rule.
     */
    public static void checkRule2(RBTree<?> rbTree) {
        if (rbTree.getRoot() != null && rbTree.getRoot().isRed())
            throw new RBTreeException("The root of the tree is red!");
    }

    /**
     * Checks if the given tree satisfies the third rule of black tree.
     * <p>
     * The third rule of a red-black tree states that no red node has a red child.
     *
     * @param rbTree the tree to check.
     * @throws RBTreeException if the tree does not satisfy the rule.
     */
    public static void checkRule3(RBTree<?> rbTree) {
        if (rbTree.getRoot() != null) checkRule3(rbTree.getRoot());
    }

    private static void checkRule3(RBNode<?> node) {
        if (node.isRed() && node.getParent().isRed())
            throw new RBTreeException("The node with key " + node.getKey() + " is red despite its parent being red!");

        if (node.hasLeft()) checkRule3(node.getLeft());
        if (node.hasRight()) checkRule3(node.getRight());
    }

    /**
     * Checks if the given tree satisfies the fourth rule of black tree.
     * <p>
     * The fourth rule of a red-black tree states that all paths from a node to a leave or half-leave contain the same number of black nodes.
     *
     * @param rbTree the tree to check.
     * @throws RBTreeException if the tree does not satisfy the rule.
     */
    public static void checkRule4(RBTree<?> rbTree) {
        if (rbTree.getRoot() != null) checkRule4(rbTree.getRoot());
    }

    private static int checkRule4(RBNode<?> node) {
        int leftSH = node.hasLeft() ? checkRule4(node.getLeft()) : 0;
        int rightSH = node.hasRight() ? checkRule4(node.getRight()) : 0;

        if (leftSH != rightSH)
            throw new RBTreeException("The children of the node with key " + node.getKey() + " do not have the same black height. Left: " + leftSH + " Right: " + rightSH);

        return node.isBlack() ? rightSH + 1 : rightSH;
    }
}
