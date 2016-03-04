import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;
import javax.inject.Singleton;
import com.googlecode.concurrenttrees.radix.*;
import com.googlecode.concurrenttrees.radix.node.concrete.SmartArrayBasedNodeFactory;

@Singleton
class Movies {
  private final RadixTree<Movie> titles;

  Movies() {
    this.titles = new ConcurrentRadixTree<>(new SmartArrayBasedNodeFactory());
  }

  public void add(Movie movie) {
    for (String w : movie.title.toLowerCase().split("\\s")) {
      titles.putIfAbsent(w, movie);
    }
  }

  public Iterable<Movie> startingWith(String s) {
    return titles.getValuesForKeysStartingWith(s);
  }
}

class MovieComparator implements Comparator<Movie> {
  public int compare(Movie a, Movie b) {
    int r = 0;
    r = r == 0 ? a.title.compareToIgnoreCase(b.title) : r;
    r = r == 0 ? a.yearReleased.compareTo(b.yearReleased) : r;
    r = r == 0 ? a.countryCode.compareTo(b.countryCode) : r;
    return r ;
  }
}
