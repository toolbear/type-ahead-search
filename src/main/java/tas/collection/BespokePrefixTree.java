package tas.collection;

import java.io.PrintWriter;
import java.util.concurrent.locks.ReentrantLock;
import java.util.*;
import java.util.stream.*;
import tas.function.*;
import static tas.function.Functions.compare;

public class BespokePrefixTree<V> implements PrefixTree<V> {
  private static final CharSequence EMPTY_SUBSEQUENCE = "";

  private final ReentrantLock writeLock;
  private Node<V> root;

  public BespokePrefixTree() {
    writeLock = new ReentrantLock(true);
    root = new Node<V>(EMPTY_SUBSEQUENCE, null, null, null);
  }

  public V putIfAbsent(CharSequence key, V value) {
    writeLock.lock();
    try {
      Tuple2<V, Node<V>> result = putIfAbsent(this.root, key, value);
      this.root = result._2;
      return result._1;
    } finally {
      writeLock.unlock();
    }
  }

  private Tuple2<V, Node<V>> putIfAbsent(Node<V> node, CharSequence key, V value) {
    Comparison comparison = compare(node.key, key);
    switch (comparison.relation) {

    case EQUIVALENT:
      if (node.isLeaf()) {
        return new Tuple2<>(node.value, node);
      } else {
        return new Tuple2<>(null, new Node<V>(key, value, node.child, node.sibling));
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

  public Iterable<CharSequence> keysStartingWith(final CharSequence prefix) {
    return new Iterable<CharSequence>(){
      public Iterator<CharSequence> iterator() {
        return new KeysStartingWith<V>(root, prefix);
      }
      @Override
      public String toString() {
        return "keys starting with " + prefix;
      }
    };
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
    out.format("%s%s── %s%s\n",
               leaders,
               node.sibling == null ? "└" : "├",
               node.key.length() == 0 ? "○" : node.key,
               node.isLeaf() ? String.format(" (%s)", node.value) : "");
    visualize(out, String.format("%s%s   ", leaders, node.sibling == null ? " " : "│"), node.child);
    visualize(out, leaders, node.sibling);
  }

  private static final class Node<V> {
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

    private final boolean isLeaf() {
      return value != null;
    }

    @Override
    public String toString() {
      return String.format("%s%s(%s)%s",
                           sibling == null ? "┕" : "┝",
                           key,
                           isLeaf() ? value : "",
                           child == null ? "" : "┑");
    }
  }

  private static final class KeysStartingWith<V> implements Iterator<CharSequence> {
    private final Deque<Node<V>> preceeding;
    private final Deque<Node<V>> match;
    private final CharSequence prefix;
    private final int prefixLength;
    private Node<V> node;
    private CharSequence next;
    private int start = 0;

    KeysStartingWith(Node<V> node, CharSequence prefix) {
      this.preceeding = new ArrayDeque<>();
      this.match = new ArrayDeque<>();
      this.node = node;
      this.prefix = prefix;
      this.prefixLength = prefix.length();
    }

    public boolean hasNext() {
      if (next != null) return true;

      // TODO: split in to two states: a) searching for match b) iterating match leaves

      while (start < prefixLength && node != null && match.isEmpty()) { // breadth first search for matching subtree
        Comparison comparison = compare(node.key, prefix.subSequence(start, prefixLength));
        start += comparison.common.length();
        switch (comparison.relation) {
        case PRECEDES: // lexicographically before prefix
          node = node.sibling;
          break;

        case LEFT_SUBSEQUENCE: // descend tree
          preceeding.addLast(node);
          node = node.child;
          break;

        case RIGHT_SUBSEQUENCE: // node and its descendants match
        case EQUIVALENT:        // ditto
          match.addLast(node);
          break;

        case LEFT_EMPTY:  // empty tree
        case RIGHT_EMPTY: // exhausted search prefix
        case SUCCEEDS:    // everything lexicographically after prefix
          node = null;
          break;

        default:
          throw new IllegalStateException("unsupported relation: " + comparison.relation.name());
        }
      }

      if (!match.isEmpty()) { // depth-first search for leaves
        while (next == null && node != null) {
          if (node.isLeaf()) {
            next = Stream.concat(preceeding.stream(), match.stream())
              .map(n -> n.key)
              .collect(Collectors.joining(""));
          }
          if (node.child != null) {
            node = node.child;
            match.addLast(node);
          } else {
            while (!match.isEmpty() && (node = match.removeLast().sibling) == null);
            if (match.isEmpty()) node = null;
            if (node != null) match.addLast(node);
          }
        }
      }

      return next != null;
    }

    public CharSequence next() {
      CharSequence result = null;
      if (next != null || hasNext()) {
        result = next;
        next = null;
      }
      return result;
    }
  }
}
