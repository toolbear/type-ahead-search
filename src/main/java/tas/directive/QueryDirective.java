package tas.directive;

import java.io.PrintWriter;
import javax.inject.Inject;
import tas.*;
import tas.io.*;

public class QueryDirective implements Directive {
  private static final int MAX_RESULTS = 10;

  private final PrintWriter out;
  private final Movies movies;

  @Inject
  QueryDirective(@StandardOutput PrintWriter out, Movies movies) {
    this.out = out;
    this.movies = movies;
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
