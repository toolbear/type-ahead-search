package tas;

import java.util.*;
import java.util.concurrent.*;
import com.google.inject.Provider;
import org.junit.*;
import org.mockito.*;
import org.mockito.junit.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;
import tas.collection.*;

public class MoviesTest {
  @Rule public MockitoRule mockito = MockitoJUnit.rule();

  @Mock private Provider<PrefixTree<ConcurrentSkipListSet<Movie>>> treeProvider;
  private Movies subject;

  @Before
  public void initializeSubject() {
    // TODO: test interactions with a PrefixTree mock instead of backing it with a real one
    when(treeProvider.get()).thenReturn(new VendorPrefixTree<ConcurrentSkipListSet<Movie>>());
    subject = new Movies(treeProvider);
  }

  @Test
  public void oneMovie() {
    Movie m = mock(Movie.class, withSettings().name("fake Amelie"));
    when(m.title()).thenReturn("Amelie");

    subject.add(m);

    assertThat(subject.startingWith("Amelie", 1)).contains(m);
    assertThat(subject.startingWith("amelie", 1)).contains(m);
    assertThat(subject.startingWith("amel", 1)).contains(m);
    assertThat(subject.startingWith("a", 1)).contains(m);
    assertThat(subject.startingWith("b", 1)).doesNotContain(m);
  }

  @Test
  public void multipleMovies() {
    /*
      1982 US The Thing
      2007 US Break a Leg
      2011 US The Thing
      2014 US American Legacies
      2014 US The Lego Movie
     */
    Movie m1 = new FatMovie("The Thing", "1982", "US");
    Movie m2 = new FatMovie("Break a Leg", "2007", "US");
    Movie m3 = new FatMovie("The Thing", "2011", "US");
    Movie m4 = new FatMovie("American Legacies", "2014", "US");
    Movie m5 = new FatMovie("The Lego Movie", "2014", "US");

    subject.add(m1);
    subject.add(m2);
    subject.add(m3);
    subject.add(m4);
    subject.add(m5);

    assertThat(subject.startingWith("L", 3))
      .hasSize(3)
      .startsWith(m4, m2, m5);

    // TODO: interesting result. Should "American Legacies" be the one returned?
    assertThat(subject.startingWith("Leg", 1))
      .hasSize(1)
      .contains(m2)
      .doesNotContain(m4, m5);

    assertThat(subject.startingWith("Thing", 2))
      .hasSize(2)
      .startsWith(m1, m3);
  }
}
