package tas.function;

public final class Functions {
  private static final CharSequence EMPTY_SEQUENCE = "";

  public static Comparison compare(CharSequence left, CharSequence right) {
    return compare(left, left.length(), right, right.length());
  }

  private static Comparison compare(CharSequence l, int llen, CharSequence r, int rlen) {
    if (llen == 0 && rlen == 0) {
      return new Comparison(EMPTY_SEQUENCE, EMPTY_SEQUENCE, EMPTY_SEQUENCE, Comparison.EQUIVALENT, false);
    } else if (llen == 0) {
      return new Comparison(EMPTY_SEQUENCE, r, EMPTY_SEQUENCE, Comparison.LEFT_EMPTY, false);
    } else if (rlen == 0) {
      return new Comparison(l, EMPTY_SEQUENCE, EMPTY_SEQUENCE, Comparison.RIGHT_EMPTY, false);
    }

    int end = 0;
    int cmp = 0;
    for (int n = Math.min(llen, rlen); end < n; end++) {
      cmp = Character.compare(l.charAt(end), r.charAt(end));
      if (cmp != 0) break;
    }
    if (llen == end && rlen == end) {
      return new Comparison(EMPTY_SEQUENCE, EMPTY_SEQUENCE, l, Comparison.EQUIVALENT, true);
    } else if (llen == end) {
      return new Comparison(EMPTY_SEQUENCE, r.subSequence(end, rlen), l, Comparison.LEFT_SUBSEQUENCE, true);
    } else if (rlen == end) {
      return new Comparison(l.subSequence(end, llen), EMPTY_SEQUENCE, r, Comparison.RIGHT_SUBSEQUENCE, true);
    } else {
      Comparison.Relation relation;
      if (cmp < 0) relation = Comparison.PRECEDES;
      else if (cmp > 0) relation = Comparison.SUCCEEDS;
      else throw new IllegalStateException("expected sequences to differ");

      return new Comparison(l.subSequence(end, llen), r.subSequence(end, rlen), l.subSequence(0, end), relation, end > 0);
    }
  }
}
