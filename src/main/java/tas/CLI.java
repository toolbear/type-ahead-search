package tas;

import java.io.*;
import java.nio.file.*;
import java.util.SortedSet;
import java.util.concurrent.*;
import tas.collection.*;
import tas.directive.*;
import tas.io.*;

public class CLI {

  public static final BufferedReader IN;
  public static final PrintWriter OUT;
  public static final PrintWriter ERR;
  public static final ExecutorService EXECUTOR_SERVICE;
  public static final FileSystem FS;
  public static final FileMethods FILEMETHODS;
  public static final Runtime RUNTIME;
  public static final MovieFactory MOVIE_FACTORY;
  public static final Provider<PrefixTree<SortedSet<Movie>>> TREE_PROVIDER;

  static {
    IN = new BufferedReader(new InputStreamReader(System.in));
    OUT = new PrintWriter(System.out);
    ERR = new PrintWriter(System.err);

    EXECUTOR_SERVICE = Executors.newWorkStealingPool();
    FS = FileSystems.getDefault();
    FILEMETHODS = new FileMethods();
    RUNTIME = Runtime.getRuntime();

    TREE_PROVIDER = new Provider<PrefixTree<SortedSet<Movie>>>(){
        public PrefixTree<SortedSet<Movie>> get() {
          return new BespokePrefixTree<SortedSet<Movie>>();
        }
     };

    /*
     * Movie with title, year, country stored as Strings
     */
    //MOVIE_FACTORY = new FatMovieFactory();
    /*
     * Movie with smaller memory footprint; supports release years [1877-2132] and 2 character country codes
     */
    MOVIE_FACTORY = new ThinMovieFactory();
  }

  public static void main(String[] args) {
    REPL repl = new REPL();
    repl.addDirective(new ProcessFileDirective());
    repl.addDirective(new QueryDirective());
    repl.addDirective(new QuitDirective());
    repl.addDirective(new VisualizeDirective());
    repl.start();
  }
}
