import java.io.*;
import org.junit.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class REPLTest {
  private BufferedReader in;
  private PrintWriter out;
  private PrintWriter err;
  private REPL subject;

  @Before
  public void mockStreams() {
    in = mock(BufferedReader.class);
    out = mock(PrintWriter.class);
    err = mock(PrintWriter.class);
  }

  @Before
  public void initializeSubject() {
    subject = new REPL(in, out, err);
  }

  @Test
  public void invalid() throws IOException {
    when(in.readLine())
      .thenReturn("nope nope nope")
      .thenReturn(null);
    subject.start();
    verify(err).println("Invalid directive: nope");
  }

  @Test
  public void directiveWithoutParameters() throws IOException {
    Directive directive = mock(Directive.class);
    when(directive.name()).thenReturn("dance");
    when(in.readLine())
      .thenReturn("dance")
      .thenReturn(null);
    subject.addDirective(directive);
    subject.start();
    verify(out).println("dance");
    verify(directive).apply("", out, err);
  }

  @Test
  public void directiveWithParameters() throws IOException {
    Directive directive = mock(Directive.class);
    when(directive.name()).thenReturn("dance");
    when(in.readLine())
      .thenReturn("dance monkeyboy dance")
      .thenReturn(null);
    subject.addDirective(directive);
    subject.start();
    verify(out).println("dance");
    verify(directive).apply("monkeyboy dance", out, err);
  }
}
