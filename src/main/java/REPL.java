import java.io.*;
import javax.inject.Inject;

class REPL {
  private BufferedReader in;
  private PrintWriter out;
  private PrintWriter err;

  @Inject
  REPL(@StandardInput BufferedReader in,
       @StandardOutput PrintWriter out,
       @StandardError PrintWriter err) {
    this.in = in;
    this.out = out;
    this.err = err;
  }

  public void start() {
    out.println("wazzup");
    out.flush();
  }
}
