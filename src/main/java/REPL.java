import java.io.*;
import java.util.*;
import java.util.concurrent.*;
import javax.inject.Inject;

class REPL {
  private final BufferedReader in;
  private final PrintWriter out;
  private final PrintWriter err;
  private final ExecutorService tasks;
  private final Runtime runtime;
  private final Map<String,Directive> directives;

  @Inject
  REPL(@StandardInput BufferedReader in,
       @StandardOutput PrintWriter out,
       @StandardError PrintWriter err,
       ExecutorService tasks,
       Runtime runtime) {
    this.in = in;
    this.out = out;
    this.err = err;
    this.tasks = tasks;
    this.runtime = runtime;
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
        if (line.trim().isEmpty()) continue;
        result = evaluate(line);
        out.flush();
        err.flush();
      }
      shutdown();
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private void shutdown() {
    tasks.shutdown();
    try {
      if (!tasks.awaitTermination(60, TimeUnit.SECONDS)) {
        tasks.shutdownNow();
        if (!tasks.awaitTermination(60, TimeUnit.SECONDS)) {
          err.println("Task(s) are hung; forcibly exiting");
          runtime.exit(1);
        }
      }
    } catch (InterruptedException e) {
      tasks.shutdownNow();
      Thread.currentThread().interrupt();
    }
  }

  private DirectiveResult evaluate(String line) {
    String[] a = line.split("\\s", 2);
    String name = a[0];
    String parameters = a.length > 1 ? a[1] : "";
    if (directives.containsKey(name)) {
      out.println(line);
      return directives.get(name).apply(parameters);
    } else {
      err.println("Invalid directive: " + name);
      return DirectiveResult.CONTINUE;
    }
  }
}
