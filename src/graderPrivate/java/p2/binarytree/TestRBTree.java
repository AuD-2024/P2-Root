package p2.binarytree;

public class TestRBTree<T extends Comparable<T>> extends RBTree<T> {

    @Override
    protected RBNode<T> createNode(T key) {
        return new TestRBNode<>(key, Color.RED);
    }
}
