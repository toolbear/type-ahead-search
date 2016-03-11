package tas;

public final class Functions {
  private static final CharSequence EMPTY_SEQUENCE = "";

  public static Comparison compare(CharSequence left, CharSequence right) {
    return compare(left, left.length(), right, right.length());
  }

  private static Comparison compare(CharSequence l, int llen, CharSequence r, int rlen) {
    if (llen == 0 && rlen == 0) {
      return new Comparison(EMPTY_SEQUENCE, EMPTY_SEQUENCE, EMPTY_SEQUENCE, Comparison.EQUIVALENT);
    } else if (llen == 0) {
      return new Comparison(EMPTY_SEQUENCE, r, EMPTY_SEQUENCE, Comparison.LEFT_EMPTY);
    } else if (rlen == 0) {
      return new Comparison(l, EMPTY_SEQUENCE, EMPTY_SEQUENCE, Comparison.RIGHT_EMPTY);
    }

    int end = 0;
    int cmp = 0;
    for (int n = Math.min(llen, rlen); end < n; end++) {
      cmp = Character.compare(l.charAt(end), r.charAt(end));
      if (cmp != 0) break;
    }
    if (llen == end && rlen == end) {
      return new Comparison(EMPTY_SEQUENCE, EMPTY_SEQUENCE, l, Comparison.EQUIVALENT);
    } else if (llen == end) {
      return new Comparison(EMPTY_SEQUENCE, r.subSequence(end, rlen), l, Comparison.LEFT_SUBSEQUENCE);
    } else if (rlen == end) {
      return new Comparison(l.subSequence(end, llen), EMPTY_SEQUENCE, r, Comparison.RIGHT_SUBSEQUENCE);
    } else {
      Comparison.Relation relation;
      if (cmp < 0) relation = Comparison.PRECEDES;
      else if (cmp > 0) relation = Comparison.SUCCEEDS;
      else throw new IllegalStateException("expected sequences to differ");

      return new Comparison(l.subSequence(end, llen), r.subSequence(end, rlen), l.subSequence(0, end), relation);
    }
  }

  public static class Comparison {
    public static final Relation LEFT_EMPTY = Relation.LEFT_EMPTY;
    public static final Relation LEFT_SUBSEQUENCE = Relation.LEFT_SUBSEQUENCE;
    public static final Relation PRECEDES = Relation.PRECEDES;
    public static final Relation EQUIVALENT = Relation.EQUIVALENT;
    public static final Relation SUCCEEDS = Relation.SUCCEEDS;
    public static final Relation RIGHT_SUBSEQUENCE = Relation.RIGHT_SUBSEQUENCE;
    public static final Relation RIGHT_EMPTY = Relation.RIGHT_EMPTY;

    public final CharSequence leftSuffix;
    public final CharSequence rightSuffix;
    public final CharSequence common;
    public final Relation relation;

    Comparison(CharSequence leftSuffix,
               CharSequence rightSuffix,
               CharSequence common,
               Relation relation) {
      this.leftSuffix = leftSuffix;
      this.rightSuffix = rightSuffix;
      this.common = common;
      this.relation = relation;
    }

    @Override
    public String toString() {
      return String.format("❮%s⌊%s⌋ %c %s⌊%s⌋❯", common, leftSuffix, relation.symbol, common, rightSuffix);
    }

    public static enum Relation {
      LEFT_EMPTY('≪', -1),
      LEFT_SUBSEQUENCE('⊂', -1),
      PRECEDES('≺', -1),
      EQUIVALENT('∼', 0),
      RIGHT_EMPTY('≫', 1),
      RIGHT_SUBSEQUENCE('⊃', 1),
      SUCCEEDS('≻', 1);

      public final char symbol;
      public final short signum;

      Relation(char symbol, int signum) {
        this.symbol = symbol;
        this.signum = (short)signum;
      }
    }
  }

  public static class Tuple2<A,B> {
    public final A _1;
    public final B _2;

    public Tuple2(A _1, B _2) {
      this._1 = _1;
      this._2 = _2;
    }
  }
}
