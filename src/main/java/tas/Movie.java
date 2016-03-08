package tas;

import java.util.Comparator;

public interface Movie extends Comparable {
  static final Comparator<Movie> COMPARATOR = new MovieComparator();

  String title();
  String yearReleased();
  String countryCode();
}

// TODO: inline
class MovieComparator implements Comparator<Movie> {
  public int compare(Movie a, Movie b) {
    int r = 0;
    r = r == 0 ? a.title().compareToIgnoreCase(b.title()) : r;
    r = r == 0 ? a.yearReleased().compareTo(b.yearReleased()) : r;
    r = r == 0 ? a.countryCode().compareTo(b.countryCode()) : r;
    return r ;
  }
}
