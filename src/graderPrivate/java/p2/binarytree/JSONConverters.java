package p2.binarytree;

import com.fasterxml.jackson.databind.JsonNode;
import org.tudalgo.algoutils.tutor.general.json.JsonConverters;
import p2.binarytree.implementation.TestBST;

import java.util.List;
import java.util.function.Function;

public class JSONConverters extends JsonConverters {

    public static TutorRBTree<Integer> toIntegerRBTree(JsonNode node) {
        return TreeParser.parseRBTree(node.asText(), Integer::parseInt, new TutorRBTree<Integer>());
    }

    public static TestBST<Integer> toIntegerBinarySearchTree(JsonNode node) {
        return TreeParser.parseBST(node.asText(), Integer::parseInt, new TestBST<Integer>());
    }

    public static AutoComplete toAutoComplete(JsonNode node) {
        return new AutoComplete(TreeParser.parseRBTree(node.asText(), Function.identity(), new TutorRBTree<>()));
    }

    public static List<Integer> toIntegerList(JsonNode node) {
        return toList(node, JsonNode::asInt);
    }

}
