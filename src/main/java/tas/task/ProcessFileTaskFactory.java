package tas.task;

import java.io.*;
import java.nio.file.Path;
import javax.inject.Inject;
import tas.*;
import tas.io.*;

// TODO: name me better.
public class ProcessFileTaskFactory {
  private final PrintWriter err;
  private final FileMethods fileMethods;
  private final MovieFactory movieFactory;
  private final Movies movies;

  @Inject
  ProcessFileTaskFactory(@StandardOutput PrintWriter err,
                          FileMethods fileMethods,
                          MovieFactory movieFactory,
                          Movies movies) {
    this.err = err;
    this.fileMethods = fileMethods;
    this.movieFactory = movieFactory;
    this.movies = movies;
  }

  public Runnable process(final Path path) {
    return new Runnable() {
      public void run() {
        try (BufferedReader reader = fileMethods.newBufferedReader(path)) {
          String line = null;
          while ((line = reader.readLine()) != null) {
            String[] a = line.split("\t");
            if (a.length == 3) {
              movies.add(movieFactory.movie(a[2], a[0], a[1]));
            }
          }
        } catch (IOException e) {
          err.format("IOException: %s%n", e);
        }
      }
    };
  }
}
