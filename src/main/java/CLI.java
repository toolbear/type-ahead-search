import java.io.*;
import com.google.inject.*;

public class CLI {
  public static void main(String[] args) {
    Injector injector = Guice.createInjector(new CLIModule());
    REPL repl = injector.getInstance(REPL.class);
    repl.start();
  }
}

class CLIModule extends AbstractModule implements Module {
  @Override
  protected void configure() {
    bind(BufferedReader.class)
      .annotatedWith(StandardInput.class)
      .toInstance(new BufferedReader(new InputStreamReader(System.in)));
    bind(PrintWriter.class)
      .annotatedWith(StandardOutput.class)
      .toInstance(new PrintWriter(System.out));
    bind(PrintWriter.class)
      .annotatedWith(StandardError.class)
      .toInstance(new PrintWriter(System.err));
  }
}
