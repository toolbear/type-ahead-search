package tas.collection;

import java.util.*;
import java.util.regex.*;

public class WordIterator implements Iterator<CharSequence> {
  private static final Pattern WORDS = Pattern.compile("[\\w-]+");

  private final CharSequence doc;
  private final Matcher matcher;
  private int start = 0;

  WordIterator(CharSequence doc) {
    this.doc = doc;
    this.matcher = WORDS.matcher(doc);
  }

  public boolean hasNext() {
    return matcher.find(start);
  }

  public CharSequence next() {
    if (matcher.find(start)) {
      start = matcher.end();
      return doc.subSequence(matcher.start(), matcher.end());
    } else {
      return null;
    }
  }
}
