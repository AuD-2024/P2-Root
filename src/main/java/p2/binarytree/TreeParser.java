package p2.binarytree;

import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Utility class for parsing strings that represent either red-black tree or a binary search tree.
 */
public class TreeParser {

    /**
     * Parses a red-black tree from a string.
     *
     * @param input The string representation of the tree.
     * @param keyParser The function used to convert the string representation of the key to its actual value.
     *
     * @return The parsed red-black tree.
     */
    public static <T extends Comparable<T>> RBTree<T> parseRBTree(String input, Function<String, T> keyParser) {
        return parseRBTree(input, keyParser, RBTree::new);
    }

    /**
     * Parses a red-black tree from a string.
     *
     * @param input The string representation of the tree.
     * @param keyParser The function used to convert the string representation of the key to its actual value.
     * @param treeFactory A factory for creating the tree.
     *
     * @return The parsed red-black tree.
     */
    public static <T extends Comparable<T>, TR extends RBTree<T>> TR parseRBTree(
        String input,
        Function<String, T> keyParser,
        Supplier<TR> treeFactory) {
        return parse(input, keyParser, treeFactory, true);
    }

    /**
     * Parses a binary search tree from a string.
     *
     * @param input The string representation of the tree.
     * @param keyParser The function used to convert the string representation of the key to its actual value.
     *
     * @return The parsed binary search tree.
     */
    public static <T extends Comparable<T>> BinarySearchTree<T> parseBST(String input, Function<String, T> keyParser) {
        return parseBST(input, keyParser, BinarySearchTree::new);
    }

    /**
     * Parses a binary search tree from a string.
     *
     * @param input The string representation of the tree.
     * @param keyParser The function used to convert the string representation of the key to its actual value.
     * @param treeFactory A factory for creating the tree.
     *
     * @return The parsed binary search tree.
     */
    public static <T extends Comparable<T>, TR extends BinarySearchTree<T>> TR parseBST(
        String input,
        Function<String, T> keyParser,
        Supplier<TR> treeFactory) {
        return parse(input, keyParser, treeFactory, false);
    }

    private static <T extends Comparable<T>,
        N extends AbstractBinaryNode<T, N>,
        TR extends AbstractBinarySearchTree<T, N>> TR parse(
            String input,
            Function<String, T> keyParser,
            Supplier<TR> treeFactory,
            boolean rb) {

        StringReader reader = new StringReader(input);

        N node = null;

        TR tree = treeFactory.get();

        if (!input.equals("[]")) node = parseNode(reader, keyParser, tree, rb);

        tree.root = node;

        if (rb) {
            if (tree.root != null) tree.root.setParent((N) ((RBTree<Integer>) tree).sentinel);
        }

        return tree;
    }

    private static <T extends Comparable<T>, N extends AbstractBinaryNode<T, N>> N parseNode(
        StringReader reader,
        Function<String, T> keyParser,
        AbstractBinarySearchTree<T, N> tree,
        boolean rbNode) {

        reader.accept('[');

        N left = null;
        N right = null;
        Color color = null;

        //left
        if (reader.peek() == '[') left = parseNode(reader, keyParser, tree, rbNode);
        reader.accept(',');

        //value
        T value = keyParser.apply(reader.readUntil(','));
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
        if (reader.peek() == '[') right = parseNode(reader, keyParser, tree, rbNode);

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
