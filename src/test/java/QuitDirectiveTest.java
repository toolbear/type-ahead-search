import java.io.*;
import org.junit.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class QuitDirectiveTest {
  Runtime runtime;
  Directive subject;

  @Before
  public void mockTheRuntime() {
    runtime = mock(Runtime.class);
  }

  @Before
  public void initializeSubject() {
    subject = new QuitDirective(runtime);
  }

  @Test
  public void exitsWithZeroStatus() {
    subject.apply("", mock(PrintWriter.class), mock(PrintWriter.class));
    verify(runtime).exit(0);
  }
}
