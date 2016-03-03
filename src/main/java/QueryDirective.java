import java.io.PrintWriter;
import javax.inject.Inject;

class QueryDirective implements Directive {
  private final PrintWriter err;

  @Inject
  QueryDirective(@StandardError PrintWriter err) {
    this.err = err;
  }

  public final String name() {
    return "query";
  }

  public DirectiveResult apply(String parameters) {
    err.println(name() + " unimplemented");
    return DirectiveResult.CONTINUE;
  }
}
