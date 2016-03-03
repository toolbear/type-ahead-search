import java.io.InputStream;
import java.io.PrintStream;
import javax.inject.Inject;

class REPL {
  private InputStream in;
  private PrintStream out;
  private PrintStream err;

  @Inject
  REPL(@StandardInput InputStream in,
               @StandardOutput PrintStream out,
               @StandardError PrintStream err) {
    this.in = in;
    this.out = out;
    this.err = err;
  }

  public void start() {
    out.println("wazzup");
  }
}
