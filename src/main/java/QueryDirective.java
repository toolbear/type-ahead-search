import java.io.PrintWriter;

class QueryDirective implements Directive {
  public final String name() {
    return "query";
  }

  public DirectiveResult apply(String parameters, PrintWriter out, PrintWriter err) {
    err.println(name() + " unimplemented");
    return DirectiveResult.CONTINUE;
  }
}
