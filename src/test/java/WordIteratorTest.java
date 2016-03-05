import java.util.*;
import org.junit.*;
import org.mockito.*;
import org.mockito.junit.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


public class WordIteratorTest {
  @Rule public MockitoRule mockito = MockitoJUnit.rule();

  @Test
  public void empty() {
    Iterator<CharSequence> subject = new WordIterator("");
    assertThat(subject.hasNext()).isFalse();
    assertThat(subject.next()).isNull();
  }

  @Test
  public void allWhitespace() {
    Iterator<CharSequence> subject = new WordIterator(" \t");
    assertThat(subject.hasNext()).isFalse();
    assertThat(subject.next()).isNull();
  }

  @Test
  @Ignore("pending")
  public void allNonWords() {}

  @Test
  public void oneWord() {
    Iterator<CharSequence> subject = new WordIterator("booyah");
    assertThat(subject.hasNext()).isTrue();
    assertThat(subject.next()).isEqualTo("booyah");
    assertThat(subject.hasNext()).isFalse();
    assertThat(subject.next()).isNull();
  }

  @Test
  @Ignore("pending")
  public void embeddedPunctuation() {}
}
