import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.inject.Singleton;
import com.googlecode.concurrenttrees.radix.*;
import com.googlecode.concurrenttrees.radix.node.concrete.SmartArrayBasedNodeFactory;

@Singleton
class Movies {
  private final RadixTree<ConcurrentSkipListSet<Movie>> titles;

  Movies() {
    this.titles = new ConcurrentRadixTree<>(new SmartArrayBasedNodeFactory());
  }

  public void add(Movie movie) {
    for (String word : movie.title.toLowerCase().split("\\s")) {
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
    for (CharSequence k : titles.getKeysStartingWith(prefix)) {
      for (Movie m : titles.getValueForExactKey(k)) {
        if (results.add(m) && results.size() >= maxResults) {
          break accumulate;
        }
      }
    }

    return Collections.unmodifiableCollection(results);
  }
}
