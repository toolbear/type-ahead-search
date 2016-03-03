import java.io.PrintWriter;

class ProcessFileDirective implements Directive {
  public final String name() {
    return "process-file";
  }

  public void apply(String parameters, PrintWriter out, PrintWriter err) {
    err.println(name() + " unimplemented");
  }
}
