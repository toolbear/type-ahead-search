import java.io.*;
import org.junit.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class QuitDirectiveTest {
  Directive subject;

  @Before
  public void initializeSubject() {
    subject = new QuitDirective();
  }

  @Test
  public void signalsShutdown() {
    DirectiveResult r = subject.apply("", mock(PrintWriter.class), mock(PrintWriter.class));
    assertThat(r).isEqualTo(DirectiveResult.SHUTDOWN);
  }
}
