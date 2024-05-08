package p2.binarytree;

public class InvalidColorException extends RuntimeException {

    public final RBNode<?> node; // used for testing

    public InvalidColorException(RBNode<?> node) {
        super("Invalid color for node with key " + node.getKey() + ": " + node.getColor());
        this.node = node;
    }

    public InvalidColorException(RBNode<?> node, String message) {
        super(message);
        this.node = node;
    }
}
