package p2.binarytree;

public class RBTreeChecker {

    public static void checkAllRules(RBTree<?> rbTree) {
        checkRule1(rbTree);
        checkRule2(rbTree);
        checkRule3(rbTree);
        checkRule4(rbTree);
    }

    public static void checkRule1(RBTree<?> rbTree) {
        if (rbTree.getRoot() != null) checkRule1(rbTree.getRoot());
    }

    private static void checkRule1(RBNode<?> node) {
        if (node.getColor() == null)
            throw new RBTreeException("Node with key " + node.getKey() + "does not have any color!");

        if (node.hasLeft()) checkRule1(node.getLeft());
        if (node.hasRight()) checkRule1(node.getRight());
    }

    public static void checkRule2(RBTree<?> rbTree) {
        if (rbTree.getRoot() != null && rbTree.getRoot().isRed())
            throw new RBTreeException("The root of the tree is red!");
    }

    public static void checkRule3(RBTree<?> rbTree) {
        if (rbTree.getRoot() != null) checkRule3(rbTree.getRoot());
    }

    private static void checkRule3(RBNode<?> node) {
        if (node.isRed() && node.getParent().isRed())
            throw new RBTreeException("The node with key " + node.getKey() + " is red despite its parent being red!");

        if (node.hasLeft()) checkRule3(node.getLeft());
        if (node.hasRight()) checkRule3(node.getRight());
    }

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
