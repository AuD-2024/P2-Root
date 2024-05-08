package p2.binarytree;

@SuppressWarnings("unused")
public class BSTNode<T extends Comparable<T>> extends BinaryNode<T, BSTNode<T>> {

    public BSTNode(T key) {
        super(key);
    }
}
