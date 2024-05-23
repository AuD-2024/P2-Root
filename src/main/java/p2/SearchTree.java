package p2;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public interface SearchTree<T extends Comparable<T>> {

    default boolean contains(T value) {
        return search(value) != null;
    }

    Node<T> search(T value);

    void insert(T value);

    default List<T> inOrder() {
        List<T> result = new ArrayList<>();
        inOrder(findSmallest(), result, Integer.MAX_VALUE, t -> true);
        return result;
    }

    void inOrder(Node<T> node, List<T> result, int max, Predicate<T> predicate);

    void findNext(Node<T> node, List<T> result, int max, Predicate<T> predicate);

    Node<T> findSmallest();

    Node<T> getRoot();
}
