import java.io.PrintWriter;

class QuitDirective implements Directive {
  public final String name() {
    return "quit";
  }

  public void apply(String parameters, PrintWriter out, PrintWriter err) {
    err.println(name() + " unimplemented");
  }
}
