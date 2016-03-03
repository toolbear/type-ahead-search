import java.io.PrintWriter;
import javax.inject.Inject;

class QuitDirective implements Directive {
  public final String name() {
    return "quit";
  }

  public DirectiveResult apply(String parameters, PrintWriter out, PrintWriter err) {
    return DirectiveResult.SHUTDOWN;
  }
}
