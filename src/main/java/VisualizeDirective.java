import java.io.PrintWriter;
import javax.inject.Inject;

class VisualizeDirective implements Directive {
  private final PrintWriter out;
  private final Movies movies;

  @Inject
  VisualizeDirective(@StandardOutput PrintWriter out, Movies movies) {
    this.out = out;
    this.movies = movies;
  }

  public final String name() {
    return "visualize";
  }

  public DirectiveResult apply(String parameters) {
    movies.visualize(out);
    return DirectiveResult.CONTINUE;
  }
}
