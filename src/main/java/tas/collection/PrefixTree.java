package tas.collection;

import java.io.PrintWriter;
import java.util.*;

public interface PrefixTree<V> {
  V putIfAbsent(CharSequence key, V value);
  Iterable<CharSequence> keysStartingWith(CharSequence prefix);
  V get(CharSequence key);

  // TODO: expose traversals instead
  void visualize(PrintWriter out);
}
