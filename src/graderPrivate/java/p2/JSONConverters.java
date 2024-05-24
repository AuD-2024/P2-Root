package p2;

import com.fasterxml.jackson.databind.JsonNode;
import org.tudalgo.algoutils.tutor.general.json.JsonConverters;
import p2.binarytree.AutoComplete;
import p2.binarytree.BinarySearchTree;
import p2.binarytree.TreeParser;

import java.util.List;
import java.util.function.Function;

public class JSONConverters extends JsonConverters {

    public static TestRBTree<Integer> toIntegerRBTree(JsonNode node)  {
        return TreeParser.parseRBTree(node.asText(), Integer::parseInt, TestRBTree::new);
    }

    public static BinarySearchTree<Integer> toIntegerBinarySearchTree(JsonNode node) {
        return TreeParser.parseBST(node.asText(), Integer::parseInt);
    }

    public static TestRBTree<String> toStringRBTree(JsonNode node) {
        return TreeParser.parseRBTree(node.asText(), Function.identity(), TestRBTree::new);
    }

    public static AutoComplete toAutoComplete(JsonNode node) {
        return TreeParser.parseRBTree(node.asText(), Function.identity(), AutoComplete::new);
    }

    public static List<Integer> toIntegerList(JsonNode node) {
        return toList(node, JsonNode::asInt);
    }

}
