package p2.binarytree;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple implementation of an auto-complete system using a red-black tree.
 * <p>
 * It works by storing a list of common words in a red-black tree and then searching for words that start with the given
 * prefix in the tree.
 */
public class AutoComplete extends RBTree<String> {

    /**
     * Returns a list of suggestions to complete the given prefix string.
     * <p>
     * It returns at most {@code max} suggestions. All returned suggestions start with the given prefix.
     *
     * @param prefix the prefix to complete.
     * @param max    the maximum number of suggestions to return.
     * @return a list of suggestions to complete the given prefix string.
     */
    public List<String> autoComplete(String prefix, int max) {
        List<String> result = new ArrayList<>();

        RBNode<String> prefixNode = prefixSearch(prefix);

        if (prefixNode == null) return List.of();

        findNext(prefixNode, result, max, str -> str.startsWith(prefix));
        return result;
    }

    /**
     * Finds the smallest node in the tree that starts with the given prefix or {@code null} if no such node exists.
     *
     * @param prefix the prefix to search for.
     * @return the smallest node in the tree that starts with the given prefix.
     */
    public RBNode<String> prefixSearch(String prefix) {

        RBNode<String> x = root;
        RBNode<String> result = null;

        while (x != null) {
            if (x.getKey().startsWith(prefix)) {
                result = x;
                x = x.getLeft();
            } else if (x.getKey().compareTo(prefix) < 0) {
                x = x.getRight();
            } else {
                x = x.getLeft();
            }
        }

        return result;
    }
}
