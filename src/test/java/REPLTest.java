import java.io.*;
import java.util.concurrent.*;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.junit.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class REPLTest {
  @Rule public MockitoRule mockito = MockitoJUnit.rule();

  private REPL subject;

  @Mock private BufferedReader in;
  @Mock private PrintWriter out;
  @Mock private PrintWriter err;
  @Mock private ExecutorService tasks;
  @Mock private Runtime runtime;

  @Before
  public void initializeSubject() {
    subject = new REPL(in, out, err, tasks, runtime);
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
    verify(directive).apply("");
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
    verify(directive).apply("monkeyboy dance");
  }

  @Test
  public void directiveSignalsShutdown() throws IOException {
    Directive directive = mock(Directive.class);
    when(directive.name()).thenReturn("dance");
    when(directive.apply(any())).thenReturn(DirectiveResult.SHUTDOWN);
    when(in.readLine())
      .thenReturn("dance")
      .thenReturn("dance")
      .thenReturn(null);
    subject.addDirective(directive);
    subject.start();
    verify(out, times(1)).println("dance");
  }
}
