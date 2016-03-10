package tas.collection;

import java.io.PrintWriter;
import tas.Functions.Tuple2;
import tas.Functions.Comparison;
import static tas.Functions.compare;

public class BespokePrefixTree<V> implements PrefixTree<V> {
  private static final CharSequence EMPTY_SUBSEQUENCE = "";

  private Node<V> root;

  public BespokePrefixTree() {
    root = new Node<V>(EMPTY_SUBSEQUENCE, null, null, null);
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
      if (node.value == null) {
        return new Tuple2<>(null, new Node<V>(key, value, node.child, node.sibling));
      } else {
        return new Tuple2<>(node.value, node);
      }

    case LEFT_EMPTY:
      return new Tuple2<>(null, new Node<V>(key, value, null, null));

    case RIGHT_EMPTY: // TODO: TEST ME
      throw new IllegalArgumentException("empty key");

    case LEFT_SUBSEQUENCE: {
      if (node.child != null) {
        Tuple2<V, Node<V>> result = putIfAbsent(node.child, comparison.rightSuffix, value);
        node.child = result._2;
        return new Tuple2<>(result._1, node);
      } else {
        Node<V> child = new Node<>(comparison.rightSuffix, value, null, null);
        Node<V> patch = new Node<>(comparison.common, node.value, child, node.sibling);
        return new Tuple2<>(null, patch);
      }
    }

    case RIGHT_SUBSEQUENCE: {
      Node<V> child = new Node<>(comparison.leftSuffix, node.value, node.child, null);
      Node<V> patch = new Node<>(comparison.common, value, child, node.sibling);
      return new Tuple2<>(null, patch);
    }

    case PRECEDES: {
      if (comparison.consanguine) {
        Node<V> right = new Node<V>(comparison.rightSuffix, value, null, null);
        Node<V> left = new Node<V>(comparison.leftSuffix, node.value, node.child, right);
        Node<V> patch = new Node<V>(comparison.common, null, left, node.sibling);
        return new Tuple2<>(null, patch);
      } else if (node.sibling != null) {
        System.out.println("BOOSH!");
        Tuple2<V, Node<V>> result = putIfAbsent(node.sibling, key, value);
        node.sibling = result._2;
        return new Tuple2<>(result._1, node);
      } else {
        Node<V> sibling = new Node<>(key, value, null, null);
        Node<V> patch = new Node<>(node.key, node.value, node.child, sibling);
        return new Tuple2<>(null, patch);
      }
    }

    case SUCCEEDS: {
      if (comparison.consanguine) {
        Node<V> left = new Node<V>(comparison.leftSuffix, node.value, node.child, null);
        Node<V> right = new Node<V>(comparison.rightSuffix, value, null, left);
        Node<V> patch = new Node<V>(comparison.common, null, right, node.sibling);
        return new Tuple2<>(null, patch);
      } else {
        Node<V> patch = new Node<>(key, value, null, node);
        return new Tuple2<>(null, patch);
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
    if (node == null) return null;
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
      if (comparison.consanguine) {
        return get(node.child, comparison.rightSuffix);
      } else {
        return get(node.sibling, key);
      }

    default:
      return null;
    }
  }

  public void visualize(PrintWriter out) {
    out.println("○");
    visualize(out, "", root);
    out.flush();
  }

  private void visualize(PrintWriter out, String leaders, Node<V> node) {
    if (node == null || node.key.length() == 0) return;
    out.format("%s%s── ○ %s%s\n",
               leaders,
               node.sibling == null ? "└" : "├", node.key,
               node.value == null ? "" : String.format(" (%s)", node.value));
    visualize(out, String.format("%s%s   ", leaders, node.sibling == null ? " " : "│"), node.child);
    visualize(out, leaders, node.sibling);
  }

  /*
   * nodes
   */

  private class Node<V> {
    private final CharSequence key;
    private final V value;
    private Node<V> child;
    private Node<V> sibling;

    Node(CharSequence key, V value, Node<V> child, Node<V> sibling) {
      this.key = key;
      this.value = value;
      this.child = child;
      this.sibling = sibling;
    }

    @Override
    public String toString() {
      return String.format("%s%s%s(%s)%s%s",
                           sibling == null ? "" : sibling, sibling == null ? "┕" : "┝",
                           key,
                           value == null ? "" : value,
                           child == null ? "" : "┑",
                           child == null ? "" : child);
    }
  }
}
