import java.io.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class REPLTest {
  private REPL subject;

  @Mock private BufferedReader in;
  @Mock private PrintWriter out;
  @Mock private PrintWriter err;

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
