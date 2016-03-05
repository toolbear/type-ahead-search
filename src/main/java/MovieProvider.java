interface MovieProvider {
  Movie movie(String title, String yearReleased, String countryCode);
}

class FatMovieProvider implements MovieProvider {
  public Movie movie(String title, String yearReleased, String countryCode) {
    return new FatMovie(title, yearReleased, countryCode);
  }
}

final class FatMovie implements Movie {
  private final String title;
  private final String yearReleased;
  private final String countryCode;

  FatMovie(String title, String yearReleased, String countryCode) {
    this.title = title;
    this.yearReleased = yearReleased;
    this.countryCode = countryCode;
  }

  public String title() {
    return title;
  }

  public String yearReleased() {
    return yearReleased;
  }

  public String countryCode() {
    return countryCode;
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
    return this.title.toLowerCase().equals(m.title().toLowerCase())
      && this.yearReleased.equals(m.yearReleased())
      && this.countryCode.equals(m.countryCode());
  }

  public int compareTo(Object o) {
    if (o == null) throw new NullPointerException();
    if (this == o) return 0;
    if (!(o instanceof Movie)) throw new ClassCastException();
    return Movie.COMPARATOR.compare(this, (Movie)o);
  }
}
