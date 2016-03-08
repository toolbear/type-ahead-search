package tas.collection;

import java.util.*;
import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;
import org.mockito.*;
import org.mockito.junit.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;


@RunWith(value = Parameterized.class)
public class PrefixTreeTest {
  @Rule public MockitoRule mockito = MockitoJUnit.rule();

  @Parameters
  public static Collection<Class> implementations() {
    return new ArrayList<Class>(Arrays.asList(
                                              VendorPrefixTree.class,
                                              BespokePrefixTree.class));
  }

  private Class<PrefixTree<Character>> implementation;
  private PrefixTree<Character> subject;

  public PrefixTreeTest(Class<PrefixTree<Character>> implementation) {
    this.implementation = implementation;
  }

  @Before
  public void initializeSubject() throws IllegalAccessException, InstantiationException {
    subject = implementation.newInstance();
  }

  @Test
  public void keyAbsent() {
    assertThat(subject.putIfAbsent("k", 'a')).isNull();
  }

  @Test
  public void keyAlreadyPresent() {
    subject.putIfAbsent("what", '?');
    assertThat(subject.putIfAbsent("what", '!')).isEqualTo('?');
  }

  @Test
  public void simpleGet() {
    subject.putIfAbsent("k", '0');
    assertThat(subject.get("k")).isEqualTo('0');
    assertThat(subject.get("key")).isNull();
  }

  @Test
  public void splitRequired() {
    subject.putIfAbsent("baggage", '1');
    subject.putIfAbsent("bag", '0');
    assertThat(subject.get("baggage")).isEqualTo('1');
    assertThat(subject.get("bag")).isEqualTo('0');
  }

  @Test
  public void growBranch() {
    subject.putIfAbsent("b", '0');
    subject.putIfAbsent("boy", '1');
    subject.putIfAbsent("boyscout", '2');
    assertThat(subject.get("b")).isEqualTo('0');
    assertThat(subject.get("boy")).isEqualTo('1');
    assertThat(subject.get("boyscout")).isEqualTo('2');
  }

  @Test
  public void disimilarBranches() {
    subject.putIfAbsent("at", '@');
    subject.putIfAbsent("star", '*');
    assertThat(subject.get("at")).isEqualTo('@');
    assertThat(subject.get("star")).isEqualTo('*');
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
