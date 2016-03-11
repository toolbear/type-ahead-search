package tas.directive;

import java.io.PrintWriter;
import tas.*;
import tas.io.*;

public class QueryDirective implements Directive {
  private static final int MAX_RESULTS = 10;

  private final PrintWriter out;
  private final Movies movies;

  QueryDirective(PrintWriter out, Movies movies) {
    this.out = out;
    this.movies = movies;
  }

  public QueryDirective() {
    this(CLI.OUT,
         Movies.singleton());
  }

  public final String name() {
    return "query";
  }

  public DirectiveResult apply(String parameters) {
    for (Movie m : movies.startingWith(parameters, MAX_RESULTS)) {
      out.println(String.format("%s\t%s\t%s", m.yearReleased(), m.countryCode(), m.title()));
    }
    return DirectiveResult.CONTINUE;
  }
}
