import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.inject.Singleton;
import com.googlecode.concurrenttrees.common.PrettyPrinter;
import com.googlecode.concurrenttrees.radix.*;
import com.googlecode.concurrenttrees.radix.node.concrete.SmartArrayBasedNodeFactory;

@Singleton
class Movies {
  private final ConcurrentRadixTree<ConcurrentSkipListSet<Movie>> titles;

  Movies() {
    this.titles = new ConcurrentRadixTree<>(new SmartArrayBasedNodeFactory());
  }

  public void add(Movie movie) {
    for (CharSequence word : new Words(movie.title().toLowerCase())) {
      ConcurrentSkipListSet<Movie> newSet = new ConcurrentSkipListSet<>();
      newSet.add(movie);
      Set<Movie> existingSet;
      if ((existingSet = titles.putIfAbsent(word, newSet)) != null) {
        existingSet.add(movie);
      }
    }
  }

  public Iterable<Movie> startingWith(String prefix, int maxResults) {
    Collection<Movie> results = new TreeSet<>();

    accumulate:
    for (CharSequence k : titles.getKeysStartingWith(prefix.toLowerCase())) {
      for (Movie m : titles.getValueForExactKey(k)) {
        if (results.add(m) && results.size() >= maxResults) {
          break accumulate;
        }
      }
    }

    return Collections.unmodifiableCollection(results);
  }

  public void visualize(Appendable out) {
    PrettyPrinter.prettyPrint(titles, out);
  }
}
