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
    bind(java.io.InputStream.class).annotatedWith(StandardInput.class).toInstance(System.in);
    bind(java.io.PrintStream.class).annotatedWith(StandardOutput.class).toInstance(System.out);
    bind(java.io.PrintStream.class).annotatedWith(StandardError.class).toInstance(System.err);
  }
}
