import java.io.*;
import java.util.*;
import javax.inject.Inject;

class REPL {
  private BufferedReader in;
  private PrintWriter out;
  private PrintWriter err;
  private Map<String,Directive> directives;

  @Inject
  REPL(@StandardInput BufferedReader in,
       @StandardOutput PrintWriter out,
       @StandardError PrintWriter err) {
    this.in = in;
    this.out = out;
    this.err = err;
    this.directives = new HashMap<>();
  }

  void addDirective(Directive d) {
    directives.put(d.name(), d);
  }

  public void start() {
    String line;
    try {
      while ((line = in.readLine()) != null) {
        evaluate(line);
        out.flush();
        err.flush();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void evaluate(String line) {
    String[] a = line.split("\\s", 2);
    String name = a[0];
    String parameters = a.length > 1 ? a[1] : "";
    if (directives.containsKey(name)) {
      out.println(name);
      directives.get(name).apply(parameters, out, err);
    } else {
      err.println("Invalid directive: " + name);
    }
  }
}
