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
  public void addSubsequence() {
    subject.putIfAbsent("baggage", '1');
    subject.putIfAbsent("bag", '0');
    assertThat(subject.get("baggage")).isEqualTo('1');
    assertThat(subject.get("bag")).isEqualTo('0');
  }

  @Test
  public void addSupersequence() {
    subject.putIfAbsent("bag", '1');
    subject.putIfAbsent("baggage", '2');
    assertThat(subject.get("bag")).isEqualTo('1');
    assertThat(subject.get("baggage")).isEqualTo('2');
  }

  @Test
  public void consanguineWithoutAncestorAdded() {
    subject.putIfAbsent("adventure", '1');
    subject.putIfAbsent("act", '2');
    assertThat(subject.get("adventure")).isEqualTo('1');
    assertThat(subject.get("act")).isEqualTo('2');
  }

  @Test
  public void fillInParent() {
    subject.putIfAbsent("subservient", '1');
    subject.putIfAbsent("substitute", '2');

    assertThat(subject.putIfAbsent("subs", '3')).isNull();

    assertThat(subject.get("subs")).isEqualTo('3');
  }

  @Test
  public void fillInAncestor() {
    subject.putIfAbsent("peterson", '1');
    subject.putIfAbsent("petechia", '2');

    assertThat(subject.putIfAbsent("pet", '3')).isNull();

    assertThat(subject.get("pet")).isEqualTo('3');
    assertThat(subject.get("pete")).isNull();
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
  public void siblingsWithNiblings() {
    subject.putIfAbsent("mothering", '1');
    subject.putIfAbsent("mother", '2');
    subject.putIfAbsent("sister", '3');
    subject.putIfAbsent("mothers", '4');
    subject.putIfAbsent("brother", '5');

    assertThat(subject.get("brother")).isEqualTo('5');
    assertThat(subject.get("mother")).isEqualTo('2');
    assertThat(subject.get("mothering")).isEqualTo('1');
    assertThat(subject.get("mothers")).isEqualTo('4');
    assertThat(subject.get("sister")).isEqualTo('3');
  }

  @Test
  public void matchAgainstEmptyTree() {
    assertThat(subject.keysStartingWith("a")).isEmpty();
  }

  @Test
  public void matchWithBlankPrefix() {
    assertThat(subject.keysStartingWith("")).isEmpty();
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
    /*
     * ○
     * ├── ca
     * │   ├── p (x)
     * │   └── t (y)
     * │       └── tle (w)
     * └── dog (z)
    */
    subject.putIfAbsent("cattle", 'w');
    subject.putIfAbsent("cap", 'x');
    subject.putIfAbsent("cat", 'y');
    subject.putIfAbsent("dog", 'z');

    assertThat(subject.keysStartingWith("ca"))
      .containsExactly("cap", "cat", "cattle");
    assertThat(subject.keysStartingWith("cat"))
      .startsWith("cat", "cattle");
    assertThat(subject.keysStartingWith("catt"))
      .startsWith("cattle");
  }

  @Test
  public void deepMatches() {
    /*
     * ○
     * └── he (3)
     *     └── r (4)
     *         └── oi
     *             ├── c (1)
     *             │   └── s (5)
     *             └── sm (2)
     */
    subject.putIfAbsent("heroic", '1');
    subject.putIfAbsent("heroism", '2');
    subject.putIfAbsent("he", '3');
    subject.putIfAbsent("her", '4');
    subject.putIfAbsent("heroics", '5');

    assertThat(subject.keysStartingWith("hero"))
      .containsExactly("heroic", "heroics", "heroism");
  }

  @Test
  public void matcWithUnmatchedSibling() {
    /*
     * ○
     * └── le
     *     ├── ague (1)
     *     └── gend (2)
     */
    subject.putIfAbsent("league", '1');
    subject.putIfAbsent("legend", '2');

    assertThat(subject.keysStartingWith("lea"))
      .containsExactly("league");
  }
}
