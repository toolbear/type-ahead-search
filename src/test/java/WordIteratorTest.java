import java.util.*;
import org.junit.*;
import org.mockito.*;
import org.mockito.junit.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


public class WordIteratorTest {
  @Rule public MockitoRule mockito = MockitoJUnit.rule();

  @Test
  public void hasNext() {
    assertThat(new WordIterator("").hasNext()).isFalse();
    Iterator<CharSequence> subject = new WordIterator("word");
    assertThat(subject.hasNext()).isTrue();
    subject.next();
    assertThat(subject.hasNext()).isFalse();
  }

  @Test
  public void next() {
    assertThat(new WordIterator("").next()).isNull();
    Iterator<CharSequence> subject = new WordIterator("word");
    assertThat(subject.next()).isNotNull();
    assertThat(subject.next()).isNull();
  }

  @Test
  public void empty() {
    assertThat(new WordIterator("")).isEmpty();
  }

  @Test
  public void justWhitespace() {
    assertThat(new WordIterator(" \t")).isEmpty();
  }

  @Test
  public void justNonWords() {
    assertThat(new WordIterator("# ? $")).isEmpty();
  }

  @Test
  public void oneWord() {
    assertThat(new WordIterator("Taken")).contains("Taken");
  }

  @Test
  public void multipleWords() {
    assertThat(new WordIterator("The Princess Bride"))
      .containsExactly("The", "Princess", "Bride");
  }

  @Test
  public void embeddedPunctuation() {
    assertThat(new WordIterator("Borat: Cultural Learnings of America For Make Benefit Glorious Nation of Kazakhstan"))
      .startsWith("Borat", "Cultural");
  }

  @Test
  public void numeric() {
    assertThat(new WordIterator("300")).startsWith("300");
  }

  @Test
  public void alphanumeric() {
    assertThat(new WordIterator("se7en")).startsWith("se7en");
  }

  @Test
  public void underscores() {
    assertThat(new WordIterator("snake_case")).startsWith("snake_case");
  }

  @Test
  public void hyphens() {
    assertThat(new WordIterator("Kick-Ass")).startsWith("Kick-Ass");
  }
}
