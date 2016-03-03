import java.io.PrintWriter;
import javax.inject.Inject;

class ProcessFileDirective implements Directive {
  private final PrintWriter err;

  @Inject
  ProcessFileDirective(@StandardError PrintWriter err) {
    this.err = err;
  }

  public final String name() {
    return "process-file";
  }

  public DirectiveResult apply(String parameters) {
    err.println(name() + " unimplemented");
    return DirectiveResult.CONTINUE;
  }
}
