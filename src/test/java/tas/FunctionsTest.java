package tas;

import java.util.function.*;
import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.assertj.core.api.Assertions.*;
import tas.Functions.Comparison;

@RunWith(Enclosed.class)
public class FunctionsTest {
  public static class ComparingCharSequences {
    private BiFunction<CharSequence,CharSequence,Comparison> subject = Functions::compare;

    @Test
    public void leftEmpty() {
      Comparison c = subject.apply("", "dog");
      assertThat(c.leftSuffix).isEmpty();
      assertThat(c.rightSuffix.toString()).isEqualTo("dog");
      assertThat(c.common).isEmpty();
      assertThat(c.relation).isEqualTo(Comparison.LEFT_EMPTY);
    }

    @Test
    public void rightEmpty() {
      Comparison c = subject.apply("dog", "");
      assertThat(c.leftSuffix.toString()).isEqualTo("dog");
      assertThat(c.rightSuffix).isEmpty();
      assertThat(c.common).isEmpty();
      assertThat(c.relation).isEqualTo(Comparison.RIGHT_EMPTY);
    }

    @Test
    public void identical() {
      Comparison c = subject.apply("dog", "dog");
      assertThat(c.leftSuffix).isEmpty();
      assertThat(c.rightSuffix).isEmpty();
      assertThat(c.common.toString()).isEqualTo("dog");
      assertThat(c.relation).isEqualTo(Comparison.EQUIVALENT);
    }

    @Test
    public void bothEmpty() {
      Comparison c = subject.apply("", "");
      assertThat(c.leftSuffix).isEmpty();
      assertThat(c.rightSuffix).isEmpty();
      assertThat(c.common).isEmpty();
      assertThat(c.relation).isEqualTo(Comparison.EQUIVALENT);
    }

    @Test
    public void leftSubSequence() {
      Comparison c = subject.apply("do", "dog");
      assertThat(c.leftSuffix).isEmpty();
      assertThat(c.rightSuffix.toString()).isEqualTo("g");
      assertThat(c.common.toString()).isEqualTo("do");
      assertThat(c.relation).isEqualTo(Comparison.LEFT_SUBSEQUENCE);
    }

    @Test
    public void rightSubSequence() {
      Comparison c = subject.apply("dog", "do");
      assertThat(c.leftSuffix.toString()).isEqualTo("g");
      assertThat(c.rightSuffix).isEmpty();
      assertThat(c.common.toString()).isEqualTo("do");
      assertThat(c.relation).isEqualTo(Comparison.RIGHT_SUBSEQUENCE);
    }

    @Test
    public void preceding() {
      Comparison c = subject.apply("aardvark", "about");
      assertThat(c.leftSuffix.toString()).isEqualTo("ardvark");
      assertThat(c.rightSuffix.toString()).isEqualTo("bout");
      assertThat(c.common.toString()).isEqualTo("a");
      assertThat(c.relation).isEqualTo(Comparison.PRECEDES);
    }

    @Test
    public void succeeding() {
      Comparison c = subject.apply("catbox", "catatonic");
      assertThat(c.leftSuffix.toString()).isEqualTo("box");
      assertThat(c.rightSuffix.toString()).isEqualTo("atonic");
      assertThat(c.common.toString()).isEqualTo("cat");
      assertThat(c.relation).isEqualTo(Comparison.SUCCEEDS);
    }

    @Test
    public void differByFirstCharacter() {
      assertThat(subject.apply("a", "b").relation).isNotEqualTo(Comparison.EQUIVALENT);
    }

    @Test
    public void differByLastCharacter() {
      assertThat(subject.apply("ab", "aa").relation).isNotEqualTo(Comparison.EQUIVALENT);
    }
  }
}
