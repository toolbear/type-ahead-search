import java.io.PrintWriter;

class ProcessFileDirective implements Directive {
  public final String name() {
    return "process-file";
  }

  public DirectiveResult apply(String parameters, PrintWriter out, PrintWriter err) {
    err.println(name() + " unimplemented");
    return DirectiveResult.CONTINUE;
  }
}
