import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;
import com.google.inject.*;

public class CLI {
  public static void main(String[] args) {
    Injector injector = Guice.createInjector(new CLIModule());
    REPL repl = injector.getInstance(REPL.class);
    repl.addDirective(injector.getInstance(ProcessFileDirective.class));
    repl.addDirective(injector.getInstance(QueryDirective.class));
    repl.addDirective(injector.getInstance(QuitDirective.class));
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
    bind(ExecutorService.class)
      .toInstance(Executors.newWorkStealingPool());
    bind(FileSystem.class)
      .toInstance(FileSystems.getDefault());
    bind(Runtime.class)
      .toInstance(Runtime.getRuntime());
  }
}

class FileMethods {
  boolean exists(Path path, LinkOption... options) {
    return Files.exists(path, options);
  }

  BufferedReader newBufferedReader(Path path) throws IOException {
    return Files.newBufferedReader(path);
  }
}
