package tas.collection;

import java.io.PrintWriter;
import tas.Functions.Tuple2;
import static tas.Functions.commonPrefix;

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
    // TODO: cyclomatic complexity is complex

    Tuple2<V, Node<V>> result;


    CharSequence common = commonPrefix(node.key, key);
    if (node.key.length() == common.length() && key.length() == common.length()) { // already present
      result = new Tuple2<>(node.value, node);
    } else if (common.length() > 0) {
      if (node.child != null) { // recurse
        Tuple2<V, Node<V>> r = putIfAbsent(node.child, key.subSequence(common.length(), key.length()), value);
        node.child = r._2;
        result = new Tuple2<>(null, node);
      } else { // split
        if (common.length() == key.length()) {
          Node<V> child = new Node<>(node.key.subSequence(common.length(), node.key.length()), node.value);
          Node<V> patch = new Node<>(common, value, child);
          result = new Tuple2<>(null, patch);
        } else {
          Node<V> child = new Node<>(key.subSequence(common.length(), key.length()), value);
          Node<V> patch = new Node<>(common, node.value, child);
          result = new Tuple2<>(null, patch);
        }
      }
    } else { // new
      result = new Tuple2<>(null, new Node<V>(key, value));
    }
    return result;
  }

  public Iterable<CharSequence> keysStartingWith(CharSequence prefix) {
    return java.util.Collections.emptySet();
  }

  public V get(CharSequence key) {
    return get(this.root, key);
  }

  private V get(Node<V> node, CharSequence key) {
    V result = null;
    CharSequence common = commonPrefix(node.key, key);
    if (node.key.length() == common.length() && key.length() == common.length()) {
      result = node.value;
    } else if (common.length() > 0 && node.child != null) {
      result = get(node.child, key.subSequence(common.length(), key.length()));
    } else {
    }
    return result;
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
      return String.format("%s(%s)", key, value == null ? "" : value);
    }
  }
}
