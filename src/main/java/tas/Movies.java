package tas;

import java.io.PrintWriter;
import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.inject.*;
import com.google.inject.Provider;
import tas.collection.*;

@Singleton
public class Movies {
  private final PrefixTree<SortedSet<Movie>> titles;

  @Inject
  Movies(Provider<PrefixTree<SortedSet<Movie>>> treeProvider) {
    this.titles = treeProvider.get();
  }

  public void add(Movie movie) {
    for (CharSequence word : new Words(movie.title().toLowerCase())) {
      SortedSet<Movie> newSet = new ConcurrentSkipListSet<>();
      newSet.add(movie);
      SortedSet<Movie> existingSet;
      if ((existingSet = titles.putIfAbsent(word, newSet)) != null) {
        existingSet.add(movie);
      }
    }
  }

  public Iterable<Movie> startingWith(String prefix, int maxResults) {
    Collection<Movie> results = new TreeSet<>();

    accumulate:
    for (CharSequence k : titles.keysStartingWith(prefix.toLowerCase())) {
      for (Movie m : titles.get(k)) {
        if (results.add(m) && results.size() >= maxResults) {
          break accumulate;
        }
      }
    }

    return Collections.unmodifiableCollection(results);
  }

  public void visualize(PrintWriter out) {
    titles.visualize(out);
  }
}
