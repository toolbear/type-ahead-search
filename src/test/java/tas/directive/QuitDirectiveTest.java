package tas.directive;

import org.junit.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import tas.*;

public class QuitDirectiveTest {
  private Directive subject;

  @Before
  public void initializeSubject() {
    subject = new QuitDirective();
  }

  @Test
  public void signalsShutdown() {
    assertThat(subject.apply("")).isEqualTo(DirectiveResult.SHUTDOWN);
  }
}
