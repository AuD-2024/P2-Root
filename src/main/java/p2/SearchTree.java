package p2;

import java.util.ArrayList;
import java.util.List;

public interface SearchTree<T extends Comparable<T>> {

    default boolean contains(T value) {
        return search(value) != null;
    }

    Node<T> search(T value);

    void insert(T value);

    void delete(T value);

    default List<T> inOrder() {
        List<T> result = new ArrayList<>();
        inOrder(findSmallest(), result, Integer.MAX_VALUE);
        return result;
    }

    void inOrder(Node<T> node, List<T> result, int max);

    Node<T> findSmallest();

    Node<T> getRoot();
}
