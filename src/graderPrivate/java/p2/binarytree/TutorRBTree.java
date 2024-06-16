package p2.binarytree;

import p2.binarytree.implementation.TestRBNode;

public class TutorRBTree<T extends Comparable<T>> extends RBTree<T> {

    private boolean overrideRotateLeft = false;
    private boolean overrideRotateRight = false;
    private boolean overrideBlackHeight = false;
    private boolean overrideFindBlackNodeWithBlackHeight = false;

    public void overrideRotateLeft() {
        overrideRotateLeft = true;
    }

    public void overrideRotateRight() {
        overrideRotateRight = true;
    }

    public void overrideBlackHeight() {
        overrideBlackHeight = true;
    }

    public void overrideFindBlackNodeWithBlackHeight() {
        overrideFindBlackNodeWithBlackHeight = true;
    }

    @Override
    public void rotateLeft(RBNode<T> x) {

        if (!overrideRotateLeft) {
            super.rotateLeft(x);
            return;
        }

        RBNode<T> y = x.getRight();
        x.setRight(y.getLeft());

        if (y.hasLeft()) {
            y.getLeft().setParent(x);
        }

        y.setParent(x.getParent());

        if (x.getParent() == sentinel) {
            root = y;
        } else if (x == x.getParent().getLeft()) {
            x.getParent().setLeft(y);
        } else {
            x.getParent().setRight(y);
        }

        y.setLeft(x);
        x.setParent(y);
    }

    @Override
    protected void rotateRight(RBNode<T> x) {

        if (!overrideRotateRight) {
            super.rotateRight(x);
            return;
        }

        RBNode<T> y = x.getLeft();
        x.setLeft(y.getRight());

        if (y.hasRight()) {
            y.getRight().setParent(x);
        }

        y.setParent(x.getParent());

        if (x.getParent() == sentinel) {
            root = y;
        } else if (x == x.getParent().getRight()) {
            x.getParent().setRight(y);
        } else {
            x.getParent().setLeft(y);
        }

        y.setRight(x);
        x.setParent(y);
    }

    @Override
    public int blackHeight() {
        if (!overrideBlackHeight) {
            return super.blackHeight();
        }
        return blackHeight(root);
    }

    private int blackHeight(RBNode<T> node) {
        if (node == null) return 0;

        return blackHeight(node.getLeft()) + (node.isBlack() ? 1 : 0);
    }

    @Override
    public RBNode<T> findBlackNodeWithBlackHeight(int targetBlackHeight, int totalBlackHeight, boolean findSmallest) {

        if (!overrideFindBlackNodeWithBlackHeight) {
            return super.findBlackNodeWithBlackHeight(targetBlackHeight, totalBlackHeight, findSmallest);
        }

        int currentBlackHeight = totalBlackHeight;
        RBNode<T> currentNode = root;

        while (currentNode.isRed() || currentBlackHeight > targetBlackHeight) {

            if (currentNode.isBlack()) currentBlackHeight--;

            if (findSmallest) currentNode = currentNode.getLeft();
            else currentNode = currentNode.getRight();
        }

        return currentNode;
    }

    @Override
    protected RBNode<T> createNode(T key) {
        return new TestRBNode<>(key, Color.RED);
    }
}
