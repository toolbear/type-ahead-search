package tas.directive;

import java.io.PrintWriter;
import tas.*;
import tas.io.*;

public class VisualizeDirective implements Directive {
  private final PrintWriter out;
  private final Movies movies;

  VisualizeDirective(PrintWriter out, Movies movies) {
    this.out = out;
    this.movies = movies;
  }

  public VisualizeDirective() {
    this(CLI.OUT,
         Movies.singleton());
  }

  public final String name() {
    return "visualize";
  }

  public DirectiveResult apply(String parameters) {
    movies.visualize(out);
    return DirectiveResult.CONTINUE;
  }
}
