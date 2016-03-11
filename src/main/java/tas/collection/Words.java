package tas.collection;

import java.util.Iterator;

public class Words implements Iterable<CharSequence> {
  private final CharSequence doc;

  public Words(CharSequence doc) {
    this.doc = doc;
  }

  public Iterator<CharSequence> iterator() {
    return new WordIterator(doc);
  }
}
