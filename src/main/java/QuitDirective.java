import java.io.PrintWriter;
import javax.inject.Inject;

class QuitDirective implements Directive {
  private final Runtime runtime;

  @Inject
  QuitDirective(Runtime runtime) {
    this.runtime = runtime;
  }

  public final String name() {
    return "quit";
  }

  public void apply(String parameters, PrintWriter out, PrintWriter err) {
    // TODO: more graceful shutdown
    runtime.exit(0);
  }
}
