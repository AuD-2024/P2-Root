package p2.binarytree;

import p2.Node;
import p2.SearchTree;

import java.util.function.BiFunction;
import java.util.function.Function;

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
    public static SearchTree<Integer> parseRBTree(String input) {
        return parseRBTree(input, RBNode::new);
    }

    /**
     * Parses a red-black tree from a string.
     *
     * @param input The string representation of the tree.
     * @param nodeFactory A factory for creating the nodes of the tree.
     *
     * @return The parsed red-black tree.
     */
    public static SearchTree<Integer> parseRBTree(String input, BiFunction<Integer, Color, ? extends RBNode<Integer>> nodeFactory) {
        return parse(input, nodeFactory, true);
    }

    /**
     * Parses a binary search tree from a string.
     *
     * @param input The string representation of the tree.
     *
     * @return The parsed binary search tree.
     */
    public static SearchTree<Integer> parseBST(String input) {
        return parseBST(input, BSTNode::new);
    }

    /**
     * Parses a binary search tree from a string.
     *
     * @param input The string representation of the tree.
     * @param nodeFactory A factory for creating the nodes of the tree.
     *
     * @return The parsed binary search tree.
     */
    public static SearchTree<Integer> parseBST(String input, Function<Integer, ? extends BSTNode<Integer>> nodeFactory) {
        return parse(input, (value, color) -> nodeFactory.apply(value), false);
    }

    private static <N extends BinaryNode<Integer, N>> SearchTree<Integer> parse(String input, BiFunction<Integer, Color, ? extends N> nodeFactory, boolean rb) {

        StringReader reader = new StringReader(input);

        Node<Integer> node = null;

        if (!input.equals("[]")) node = parseNode(reader, nodeFactory, rb);

        if (rb) {
            RBTree<Integer> tree = new RBTree<>();
            tree.root = (RBNode<Integer>) node;
            if (tree.root != null) tree.root.setParent(tree.sentinel);
            return tree;
        } else {
            BinarySearchTree<Integer> tree = new BinarySearchTree<>();
            tree.root = (BSTNode<Integer>) node;
            return tree;
        }
    }

    private static <N extends BinaryNode<Integer, N>> N parseNode(StringReader reader, BiFunction<Integer, Color, ? extends N> nodeFactory, boolean rbNode) {
        reader.accept('[');

        N left = null;
        N right = null;
        Color color = null;

        //left
        if (reader.peek() == '[') left = parseNode(reader, nodeFactory, rbNode);
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
        if (reader.peek() == '[') right = parseNode(reader, nodeFactory, rbNode);

        reader.accept(']');

        N node = nodeFactory.apply(value, color);

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
