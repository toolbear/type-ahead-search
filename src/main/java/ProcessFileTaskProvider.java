import java.io.*;
import java.nio.file.Path;
import javax.inject.Inject;

// TODO: name me better.
class ProcessFileTaskProvider {
  private final PrintWriter err;
  private final FileMethods fileMethods;
  private final Movies movies;

  @Inject
  ProcessFileTaskProvider(@StandardOutput PrintWriter err, FileMethods fileMethods, Movies movies) {
    this.err = err;
    this.fileMethods = fileMethods;
    this.movies = movies;
  }

  Runnable process(final Path path) {
    return new Runnable() {
      public void run() {
        try (BufferedReader reader = fileMethods.newBufferedReader(path)) {
          String line = null;
          while ((line = reader.readLine()) != null) {
            String[] a = line.split("\t");
            if (a.length == 3) {
              movies.add(new Movie(a[2], a[0], a[1]));
            }
          }
        } catch (IOException e) {
          err.format("IOException: %s%n", e);
        }
      }
    };
  }
}
