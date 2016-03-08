package tas;

public final class Functions {
  public static class Tuple2<A,B> {
    public final A _1;
    public final B _2;
    public Tuple2(A _1, B _2) {
      this._1 = _1;
      this._2 = _2;
    }
  }

  public static CharSequence commonPrefix(CharSequence a, CharSequence b) {
    int end = 0;
    for (int n = Math.min(a.length(), b.length()); end < n; end++) {
      if (a.charAt(end) != b.charAt(end)) break;
    }
    return a.subSequence(0, end);
  }
}
