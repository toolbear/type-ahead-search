import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;
import org.junit.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProcessFileDirectiveTest {
  private PrintWriter err;
  private ExecutorService executorService;
  private ProcessFileTaskProvider taskProvider;
  private FileSystem fileSystem;
  private FileMethods fileMethods;
  private Directive subject;

  @Before
  public void mockIO() {
    err = mock(PrintWriter.class);
    fileSystem = mock(FileSystem.class);
    fileMethods = mock(FileMethods.class);
  }

  @Before
  public void mockConcurrency() {
    executorService = mock(ExecutorService.class);
    taskProvider = mock(ProcessFileTaskProvider.class);
  }

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
