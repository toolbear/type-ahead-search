package tas.task;

import java.io.*;
import java.nio.file.*;
import java.util.concurrent.*;
import org.junit.*;
import org.mockito.junit.*;
import org.mockito.Mock;
import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import tas.*;
import tas.io.*;

public class ProcessFileTaskProviderTest {
  @Rule public MockitoRule mockito = MockitoJUnit.rule();

  private ProcessFileTaskProvider subject;

  @Mock private Path path;
  @Mock private PrintWriter err;
  @Mock private FileMethods fileMethods;
  @Mock private BufferedReader reader;
  @Mock private MovieProvider movieProvider;
  @Mock private Movies movies;

  @Before
  public void mockIO() throws IOException {
    when(fileMethods.newBufferedReader(any())).thenReturn(reader);
  }

  @Before
  public void initializeSubject() {
    subject = new ProcessFileTaskProvider(err, fileMethods, movieProvider, movies);
  }

  @Test
  public void logsException() throws IOException {
    Exception boom = new IOException("Big bada boom!");
    when(reader.readLine()).thenThrow(boom);

    subject.process(path).run();

    verify(err).format(contains("IOException"), same(boom));
    verifyZeroInteractions(movies);
  }

  @Test
  public void emptyFile() throws IOException {
    when(reader.readLine()).thenReturn(null);

    subject.process(path).run();

    verifyZeroInteractions(movies);
  }

  @Test
  public void invalidEntry() throws IOException {
    when(reader.readLine())
      .thenReturn("1997\tContact")
      .thenReturn(null);

    subject.process(path).run();

    verifyZeroInteractions(movies);
  }

  @Test
  public void oneEntry() throws IOException {
    Movie m = mock(Movie.class, withSettings().name("fake Contact"));

    when(reader.readLine())
      .thenReturn("1997\tUS\tContact")
      .thenReturn(null);
    when(movieProvider.movie("Contact", "1997", "US")).thenReturn(m);

    subject.process(path).run();

    verify(movies).add(m);
  }

  @Test
  public void multipleEntries() throws IOException {
    Movie m1 = mock(Movie.class, withSettings().name("fake Lola (US)"));
    Movie m2 = mock(Movie.class, withSettings().name("fake Lola (DE)"));

    when(reader.readLine())
      .thenReturn("1998\tUS\tRun Lola Run")
      .thenReturn("1998\tDE\tLola rennt")
      .thenReturn(null);
    when(movieProvider.movie(any(), any(), eq("US"))).thenReturn(m1);
    when(movieProvider.movie(any(), any(), eq("DE"))).thenReturn(m2);

    subject.process(path).run();

    verify(movies).add(m1);
    verify(movies).add(m2);
  }

  @Test
  public void validAndInvalidEntries() throws IOException {
    when(reader.readLine())
      .thenReturn("2016\tUS\tValid A")
      .thenReturn("2016\tUS\tInvalid\tExtra")
      .thenReturn("2016\tUS\tValid B")
      .thenReturn(null);

    subject.process(path).run();

    verify(movies, times(2)).add(any(Movie.class));
  }
}
