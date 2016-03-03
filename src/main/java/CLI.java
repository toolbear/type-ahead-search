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
    bind(REPL.class).to(TerminalREPL.class);
  }
}
