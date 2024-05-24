package p2;

import com.fasterxml.jackson.databind.JsonNode;
import p2.binarytree.BinarySearchTree;
import p2.binarytree.RBTree;
import p2.binarytree.TreeParser;

public class JSONConverters {

    public static TestRBTree<Integer> toRBTree(JsonNode node)  {
        return TreeParser.parseRBTree(node.asText(), TestRBTree::new);
    }

    public static BinarySearchTree<Integer> toBinarySearchTree(JsonNode node) {
        return TreeParser.parseBST(node.asText());
    }
}
