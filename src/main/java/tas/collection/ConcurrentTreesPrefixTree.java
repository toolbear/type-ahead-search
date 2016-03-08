package tas.collection;

import java.io.PrintWriter;
import com.googlecode.concurrenttrees.common.PrettyPrinter;
import com.googlecode.concurrenttrees.radix.*;
import com.googlecode.concurrenttrees.radix.node.concrete.SmartArrayBasedNodeFactory;

public class ConcurrentTreesPrefixTree<V> implements PrefixTree<V> {
  private final ConcurrentRadixTree<V> tree;

  public ConcurrentTreesPrefixTree() {
    this.tree = new ConcurrentRadixTree<>(new SmartArrayBasedNodeFactory());
  }

  public V putIfAbsent(CharSequence key, V value) {
    return tree.putIfAbsent(key, value);
  }

  public Iterable<CharSequence> keysStartingWith(CharSequence prefix) {
    return tree.getKeysStartingWith(prefix);
  }

  public V get(CharSequence key) {
    return tree.getValueForExactKey(key);
  }

  public void visualize(PrintWriter out) {
    PrettyPrinter.prettyPrint(tree, out);
  }
}
