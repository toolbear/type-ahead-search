import java.nio.file.Path;

class ProcessFileTaskProvider {
  Runnable process(Path path) {
    return new Runnable() {
      public void run() {}
    };
  }
}
