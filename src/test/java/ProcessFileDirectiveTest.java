import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ProcessFileDirectiveTest {
  private Directive subject;

  @Mock private PrintWriter err;
  @Mock private ExecutorService executorService;
  @Mock private ProcessFileTaskProvider taskProvider;
  @Mock private FileSystem fileSystem;
  @Mock private FileMethods fileMethods;

  @Before
  public void initializeSubject() {
    subject = new ProcessFileDirective(err, fileSystem, fileMethods, executorService, taskProvider);
  }

  @Test
  public void signalsContinue() {
    assertThat(subject.apply("")).isEqualTo(DirectiveResult.CONTINUE);
  }

  @Test
  public void pathToNamedFile() {
    subject.apply("I'm blocking you out");
    verify(fileSystem).getPath("I'm blocking you out");
  }

  @Test
  public void taskForExistingPath() {
    Path path = mock(Path.class);
    when(fileSystem.getPath("I'm crushing your head!")).thenReturn(path);
    when(fileMethods.exists(path)).thenReturn(true);

    subject.apply("I'm crushing your head!");

    verify(taskProvider).process(path);
  }

  @Test
  public void schedulesTask() {
    Runnable task = mock(Runnable.class);
    when(fileMethods.exists(any())).thenReturn(true);
    when(taskProvider.process(any())).thenReturn(task);

    subject.apply("I'm pinching your face.");

    verify(executorService).submit(task);
  }

  @Test
  public void pathNotFound() {
    subject.apply("Nobody home");
    verify(err).println("No such file: Nobody home");
    verify(executorService, never()).submit(any(Runnable.class));
  }
}
