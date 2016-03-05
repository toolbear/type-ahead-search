import java.util.*;
import java.util.regex.*;

class WordIterator implements Iterator<CharSequence> {
  private static final Pattern WORDS = Pattern.compile("\w+");

  private final CharSequence doc;
  private Matcher matcher;
  
  WordIterator(CharSequence doc) {
    this.doc = doc;
    this.matcher = WORDS.matcher
  }

  public boolean hasNext() {
    for (int i = start, n = doc.length(); i < n; i++) {
      char ch = doc.charAt(i);
      System.out.println(ch);
      if (ch >= 'a' && ch <= 'Z') return true;
    }
    return false;
  }

  public CharSequence next() {
    return null;
  }
}
