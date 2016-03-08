import java.util.Iterator;

class Words implements Iterable<CharSequence> {
  private final CharSequence doc;

  Words(CharSequence doc) {
    this.doc = doc;
  }

  public Iterator<CharSequence> iterator() {
    return new WordIterator(doc);
  }
}
