import java.io.*;
import org.junit.*;
import org.mockito.Mock;
import org.mockito.junit.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


public class QueryDirectiveTest {
  @Rule public MockitoRule mockito = MockitoJUnit.rule();

  private Directive subject;

  @Mock private PrintWriter out;
  @Mock private Movies movies;

  @Before
  public void initializeSubject() {
    subject = new QueryDirective(out, movies);
  }

  @Test
  public void signalsContinue() {
    assertThat(subject.apply("q")).isEqualTo(DirectiveResult.CONTINUE);
  }
}
