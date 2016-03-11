package tas;

import java.io.*;
import java.nio.file.*;
import java.util.SortedSet;
import java.util.concurrent.*;
import com.google.inject.*;
import tas.collection.*;
import tas.directive.*;
import tas.io.*;

public class CLI {
  public static void main(String[] args) {
    Injector injector = Guice.createInjector(new CLIModule());
    REPL repl = injector.getInstance(REPL.class);
    repl.addDirective(injector.getInstance(ProcessFileDirective.class));
    repl.addDirective(injector.getInstance(QueryDirective.class));
    repl.addDirective(injector.getInstance(QuitDirective.class));
    repl.addDirective(injector.getInstance(VisualizeDirective.class));
    repl.start();
  }
}

class CLIModule extends AbstractModule implements Module {
  @Override
  protected void configure() {
    /*
     * Movie with title, year, country stored as Strings
     */
    // bind(MovieFactory.class).to(FatMovieFactory.class);
    /*
     * Movie with smaller memory footprint; supports release years [1877-2132] and 2 character country codes
     */
    bind(MovieFactory.class).to(ThinMovieFactory.class);

    bind(new TypeLiteral<PrefixTree<SortedSet<Movie>>>(){}).toProvider(new Provider<PrefixTree<SortedSet<Movie>>>(){
        public PrefixTree<SortedSet<Movie>> get() {
          return new BespokePrefixTree<SortedSet<Movie>>();
        }
      });

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
