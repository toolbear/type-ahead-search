package tas;

import java.util.function.*;
import org.junit.*;
import org.junit.experimental.runners.Enclosed;
import org.junit.runner.RunWith;
import static org.assertj.core.api.Assertions.*;

@RunWith(Enclosed.class)
public class FunctionsTest {

  public static class ComparingCharSequences {
    private BiFunction<CharSequence,CharSequence,CharSequence> subject = Functions::commonPrefix;

    @Test
    public void bothEmpty() {
      assertThat(subject.apply("", "").toString()).isEmpty();
    }

    @Test
    public void leftEmpty() {
      assertThat(subject.apply("", "dog").toString()).isEmpty();
    }

    @Test
    public void rightEmpty() {
      assertThat(subject.apply("dog", "").toString()).isEmpty();
    }

    @Test
    public void identical() {
      assertThat(subject.apply("dog", "dog").toString()).isEqualTo("dog");
    }

    @Test
    public void leftSubSequence() {
      assertThat(subject.apply("do", "dog").toString()).isEqualTo("do");
    }

    @Test
    public void rightSubSequence() {
      assertThat(subject.apply("dog", "do").toString()).isEqualTo("do");
    }
  }
}
