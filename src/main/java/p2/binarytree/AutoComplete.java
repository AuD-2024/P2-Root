package p2.binarytree;

import java.util.List;

public class AutoComplete extends RBTree<String> {

    public List<String> autoComplete(String prefix, int max) {
        return findNext(prefixSearch(prefix), max);
    }

    private RBNode<String> prefixSearch(String value) {

        RBNode<String> x = root;
        RBNode<String> result = null;

        while (x != null) {
            if (x.getKey().startsWith(value)) {
                result = x;
                x = x.getLeft();
            } else if (x.getKey().compareTo(value) < 0) {
                x = x.getRight();
            } else {
                x = x.getLeft();
            }
        }

        return result;

    }
}
