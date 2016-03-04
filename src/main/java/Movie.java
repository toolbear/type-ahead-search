import java.util.Comparator;

class Movie implements Comparable {
  static final Comparator<Movie> COMPARATOR = new MovieComparator();

  final String title;
  final String yearReleased;
  final String countryCode;

  Movie(String title, String yearReleased, String countryCode) {
    this.title = title;
    this.yearReleased = yearReleased;
    this.countryCode = countryCode;
  }

  @Override
  public String toString() {
    return String.format("%s (%s) (%s)", title, yearReleased, countryCode);
  }

  @Override
  public boolean equals(Object o) {
    if (o == null) return false;
    if (this == o) return true;
    return o instanceof Movie ? this.equalsMovie((Movie)o) : false;
  }

  @Override
  public int hashCode() {
    int result = 7;
    result = 19 * result + title.toLowerCase().hashCode();
    result = 19 * result + yearReleased.hashCode();
    result = 19 * result + countryCode.hashCode();
    return result;
  }

  boolean equalsMovie(Movie m) {
    return this.title.toLowerCase().equals(m.title.toLowerCase())
      && this.yearReleased.equals(m.yearReleased)
      && this.countryCode.equals(m.countryCode);
  }

  public int compareTo(Object o) {
    if (o == null) throw new NullPointerException();
    if (this == o) return 0;
    if (!(o instanceof Movie)) throw new ClassCastException();
    return COMPARATOR.compare(this, (Movie)o);
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
