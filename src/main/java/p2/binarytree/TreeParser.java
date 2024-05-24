package p2.binarytree;

import java.util.function.Supplier;

/**
 * Utility class for parsing strings that represent either red-black tree or a binary search tree.
 */
public class TreeParser {

    /**
     * Parses a red-black tree from a string.
     *
     * @param input The string representation of the tree.
     *
     * @return The parsed red-black tree.
     */
    public static RBTree<Integer> parseRBTree(String input) {
        return parseRBTree(input, RBTree::new);
    }

    /**
     * Parses a red-black tree from a string.
     *
     * @param input The string representation of the tree.
     * @param nodeFactory A factory for creating the nodes of the tree.
     *
     * @return The parsed red-black tree.
     */
    public static <T extends RBTree<Integer>> T parseRBTree(String input, Supplier<T> treeFactory) {
        return parse(input, treeFactory, true);
    }

    /**
     * Parses a binary search tree from a string.
     *
     * @param input The string representation of the tree.
     *
     * @return The parsed binary search tree.
     */
    public static BinarySearchTree<Integer> parseBST(String input) {
        return parseBST(input, BinarySearchTree::new);
    }

    /**
     * Parses a binary search tree from a string.
     *
     * @param input The string representation of the tree.
     * @param nodeFactory A factory for creating the nodes of the tree.
     *
     * @return The parsed binary search tree.
     */
    public static <T extends BinarySearchTree<Integer>> T parseBST(String input, Supplier<T> treeFactory) {
        return parse(input, treeFactory, false);
    }

    private static <N extends BinaryNode<Integer, N>, T extends AbstractBinarySearchTree<Integer, N>> T parse(String input, Supplier<T> treeFactory, boolean rb) {

        StringReader reader = new StringReader(input);

        N node = null;

        T tree = treeFactory.get();

        if (!input.equals("[]")) node = parseNode(reader, tree, rb);

        tree.root = node;

        if (rb) {
            if (tree.root != null) tree.root.setParent((N) ((RBTree<Integer>) tree).sentinel);
        }

        return tree;
    }

    private static <N extends BinaryNode<Integer, N>> N parseNode(StringReader reader, AbstractBinarySearchTree<Integer, N> tree, boolean rbNode) {
        reader.accept('[');

        N left = null;
        N right = null;
        Color color = null;

        //left
        if (reader.peek() == '[') left = parseNode(reader, tree, rbNode);
        reader.accept(',');

        //value
        int value = Integer.parseInt(reader.readUntil(','));
        reader.accept(',');

        //color
        if (rbNode) {
            String colorString = reader.readUntil(',');

            color = switch (colorString) {
                case "R" -> Color.RED;
                case "B" -> Color.BLACK;
                case "null" -> null;
                default -> throw new IllegalArgumentException("Invalid color: " + colorString);
            };

            reader.accept(',');
        }

        //right
        if (reader.peek() == '[') right = parseNode(reader, tree, rbNode);

        reader.accept(']');

        N node = tree.createNode(value);

        if (rbNode) ((RBNode<Integer>) node).setColor(color);

        node.setLeft(left);
        node.setRight(right);

        if (left != null) left.setParent(node);
        if (right != null) right.setParent(node);

        return node;
    }

    private static class StringReader {

        private final String input;
        private int index;

        public StringReader(String input) {
            this.input = input;
        }

        public boolean hasNext() {
            skipWhitespace();
            return index < input.length();
        }

        public char next() {
            skipWhitespace();
            return input.charAt(index++);
        }

        public char peek() {
            skipWhitespace();
            return input.charAt(index);
        }

        public void skipWhitespace() {
            while (index < input.length() && Character.isWhitespace(input.charAt(index))) {
                index++;
            }
        }

        public void accept(char expected) {
            char next = next();
            if (next != expected) {
                throw new IllegalArgumentException("Expected: `" + expected + "`, found: `" + next + "`");
            }
        }

        public String readUntil(char delimiter) {
            StringBuilder builder = new StringBuilder();
            while (hasNext() && peek() != delimiter) {
                builder.append(next());
            }
            return builder.toString();
        }

    }

}
