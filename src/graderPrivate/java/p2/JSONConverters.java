package p2;

import com.fasterxml.jackson.databind.JsonNode;
import p2.binarytree.BinarySearchTree;
import p2.binarytree.RBTree;
import p2.binarytree.TreeParser;

public class JSONConverters {

    public static RBTree<Integer> toRBTree(JsonNode node)  {
        return (RBTree<Integer>) TreeParser.parseRBTree(node.asText()); //TODO create test RB tree
    }

    public static BinarySearchTree<Integer> toBinarySearchTree(JsonNode node) {
        return (BinarySearchTree<Integer>) TreeParser.parseBST(node.asText());
    }
}
