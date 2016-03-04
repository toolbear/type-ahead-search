import java.io.*;
import java.util.*;
import org.junit.*;
import org.mockito.*;
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

  @Test
  public void searchesForTitles() {
    subject.apply("pre");
    verify(movies).startingWith(eq("pre"), anyInt());
  }

  @Test
  public void limitsNumberOfResults() {
    subject.apply("whatever");
    verify(movies).startingWith(any(), eq(10));
  }

  @Test
  public void noMatches() {
    when(movies.startingWith(any(), anyInt())).thenReturn(Collections.emptySet());
    subject.apply("whatever");
    verifyZeroInteractions(out);
  }

  @Test
  public void outputsMatchesInOrderReturned() {
    InOrder inOrder = inOrder(out);
    when(movies.startingWith(any(), anyInt()))
      .thenReturn(Arrays.asList(
                                new Movie("A", "2016", "US"),
                                new Movie("B", "2015", "FR")));

    subject.apply("whatever");

    inOrder.verify(out).println("2016\tUS\tA");
    inOrder.verify(out).println("2015\tFR\tB");
  }
}
