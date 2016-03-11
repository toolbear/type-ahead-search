package tas.io;

import java.io.*;
import java.nio.file.*;

public class FileMethods {
  public boolean exists(Path path, LinkOption... options) {
    return Files.exists(path, options);
  }

  public BufferedReader newBufferedReader(Path path) throws IOException {
    return Files.newBufferedReader(path);
  }
}
