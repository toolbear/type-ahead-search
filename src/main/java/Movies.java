import java.util.*;
import java.util.concurrent.ConcurrentSkipListSet;

class Movies {
  private final NavigableSet<Movie> movies;

  Movies() {
    this.movies = new ConcurrentSkipListSet(new MovieComparator());
  }

  public void add(Movie movie) {
    this.movies.add(movie);
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
