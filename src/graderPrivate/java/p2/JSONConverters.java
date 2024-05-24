package p2;

import com.fasterxml.jackson.databind.JsonNode;
import p2.binarytree.BinarySearchTree;
import p2.binarytree.TreeParser;

import java.util.function.Function;

public class JSONConverters {

    public static TestRBTree<Integer> toIntegerRBTree(JsonNode node)  {
        return TreeParser.parseRBTree(node.asText(), Integer::parseInt, TestRBTree::new);
    }

    public static BinarySearchTree<Integer> toIntegerBinarySearchTree(JsonNode node) {
        return TreeParser.parseBST(node.asText(), Integer::parseInt);
    }

    public static TestRBTree<String> toStringRBTree(JsonNode node) {
        return TreeParser.parseRBTree(node.asText(), Function.identity(), TestRBTree::new);
    }

}
