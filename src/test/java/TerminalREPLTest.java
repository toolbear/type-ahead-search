import org.junit.Test;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class TerminalREPLTest {
  @Test
  public void equality() {
    assertThat("blue").isEqualTo("blue");
  }

  @Test
  public void fakery() {
    A a = mock(A.class);
   a.b();
    verify(a).b();
  }
}

interface A {
  void b();
}
