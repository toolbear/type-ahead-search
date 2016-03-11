package tas.collection;

import java.io.PrintWriter;
import tas.Functions.Tuple2;
import tas.Functions.Comparison;
import static tas.Functions.compare;

public class BespokePrefixTree<V> implements PrefixTree<V> {
  private static final CharSequence EMPTY_KEY = "";

  private Node<V> root;

  public BespokePrefixTree() {
    root = new Node<V>(EMPTY_KEY, null);
  }

  public V putIfAbsent(CharSequence key, V value) {
    Tuple2<V, Node<V>> result = putIfAbsent(this.root, key, value);
    this.root = result._2;
    return result._1;
  }

  private Tuple2<V, Node<V>> putIfAbsent(Node<V> node, CharSequence key, V value) {

    Comparison comparison = compare(node.key, key);
    switch (comparison.relation) {

    case EQUIVALENT:
      return new Tuple2<>(node.value, node);

    case LEFT_EMPTY:
      return new Tuple2<>(null, new Node<V>(key, value));

    case RIGHT_EMPTY: // TODO: TEST ME
      throw new IllegalArgumentException("empty key");

    case LEFT_SUBSEQUENCE: {
      if (node.child != null) {
        Tuple2<V, Node<V>> result = putIfAbsent(node.child, comparison.rightSuffix, value);
        node.child = result._2;
        return new Tuple2<>(result._1, node);
      } else {
        Node<V> child = new Node<>(comparison.rightSuffix, value);
        Node<V> patch = new Node<>(comparison.common, node.value, child);
        return new Tuple2<>(null, patch);
      }
    }

    case RIGHT_SUBSEQUENCE: {
      Node<V> child = new Node<>(comparison.leftSuffix, node.value);
      Node<V> patch = new Node<>(comparison.common, value, child);
      return new Tuple2<>(null, patch);
    }

    case PRECEDES:
    case SUCCEEDS: {
      if (comparison.common.length() > 0) {
        Tuple2<V, Node<V>> result = putIfAbsent(node.child, comparison.rightSuffix, value);
        node.child = result._2;
        return new Tuple2<>(null, node);
      } else { // new
        return new Tuple2<>(null, new Node<V>(key, value));
      }
    }

    default:
      throw new IllegalStateException("unsupported relation: " + comparison.relation.name());
    }
  }

  public Iterable<CharSequence> keysStartingWith(CharSequence prefix) {
    return java.util.Collections.emptySet();
  }

  public V get(CharSequence key) {
    return get(this.root, key);
  }

  private V get(Node<V> node, CharSequence key) {
    Comparison comparison = compare(node.key, key);
    switch (comparison.relation) {

    case EQUIVALENT:
      return node.value;

    case RIGHT_EMPTY: // TODO: TEST ME
      throw new IllegalArgumentException("empty key");

    case LEFT_SUBSEQUENCE:
    case RIGHT_SUBSEQUENCE:
    case PRECEDES:
    case SUCCEEDS:
      if (comparison.common.length() > 0 && node.child != null) {
        return get(node.child, comparison.rightSuffix);
      } else {
        return null;
      }

    default:
      return null;
    }
  }

  public void visualize(PrintWriter out) {
    // TODO: pretty print us
    out.println("not implemented");
  }

  /*
   * nodes
   */

  private class Node<V> {
    private final CharSequence key;
    private final V value;
    private Node<V> child;

    Node(CharSequence key, V value) {
      this(key, value, null);
    }

    Node(CharSequence key, V value, Node<V> child) {
      this.key = key;
      this.value = value;
      this.child = child;
    }

    @Override
    public String toString() {
      return String.format("%s(%s)%s", key, value == null ? "" : value, child == null ? "" : "┕");
    }
  }
}
