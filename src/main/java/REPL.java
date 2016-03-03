import java.io.*;
import java.util.*;
import javax.inject.Inject;

class REPL {
  private final BufferedReader in;
  private final PrintWriter out;
  private final PrintWriter err;
  private final Map<String,Directive> directives;

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
    DirectiveResult result = DirectiveResult.CONTINUE;
    try {
      while (result == DirectiveResult.CONTINUE && (line = in.readLine()) != null) {
        result = evaluate(line);
        out.flush();
        err.flush();
      }
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private DirectiveResult evaluate(String line) {
    String[] a = line.split("\\s", 2);
    String name = a[0];
    String parameters = a.length > 1 ? a[1] : "";
    if (directives.containsKey(name)) {
      out.println(name);
      return directives.get(name).apply(parameters, out, err);
    } else {
      err.println("Invalid directive: " + name);
      return DirectiveResult.CONTINUE;
    }
  }
}
