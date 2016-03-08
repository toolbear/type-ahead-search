package tas.collection;

import org.junit.*;
import org.mockito.*;
import org.mockito.junit.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


public class PrefixTreeTest {
  @Rule public MockitoRule mockito = MockitoJUnit.rule();

  private PrefixTree<Character> subject;

  @Before
  public void initializeSubject() {
    subject = new ConcurrentTreesPrefixTree<>();
  }

  @Test
  public void keyAbsent() {
    assertThat(subject.putIfAbsent("k", 'a')).isNull();
  }

  @Test
  public void keyAlreadyPresent() {
    subject.putIfAbsent("k", 'a');
    assertThat(subject.putIfAbsent("k", 'b')).isEqualTo('a');
  }

  @Test
  public void getting() {
    subject.putIfAbsent("k", 'a');
    assertThat(subject.get("k")).isEqualTo('a');
  }

  @Test
  public void prefixMatching() {
    subject.putIfAbsent("cat", 'z');
    assertThat(subject.keysStartingWith("b")).doesNotContain("cat");    
    assertThat(subject.keysStartingWith("c")).contains("cat");
    assertThat(subject.keysStartingWith("ca")).contains("cat");
    assertThat(subject.keysStartingWith("cat")).contains("cat");
    assertThat(subject.keysStartingWith("cata")).doesNotContain("cat");
    assertThat(subject.keysStartingWith("d")).doesNotContain("cat");
  }

  @Test
  public void multipleMatches() {
    subject.putIfAbsent("cattle", 'w');
    subject.putIfAbsent("cap", 'x');
    subject.putIfAbsent("cat", 'y');
    subject.putIfAbsent("dog", 'z');

    assertThat(subject.keysStartingWith("ca"))
      .containsExactly("cap", "cat", "cattle");
    assertThat(subject.keysStartingWith("cat"))
      .containsExactly("cat", "cattle");
    assertThat(subject.keysStartingWith("catt"))
      .containsExactly("cattle");
  }
}
