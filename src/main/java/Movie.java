class Movie {
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
}
