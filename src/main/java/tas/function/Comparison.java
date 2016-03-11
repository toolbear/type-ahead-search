package tas.function;

public class Comparison {
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
  public final boolean consanguine;

  Comparison(CharSequence leftSuffix,
             CharSequence rightSuffix,
             CharSequence common,
             Relation relation,
             boolean consanguine) {
    this.leftSuffix = leftSuffix;
    this.rightSuffix = rightSuffix;
    this.common = common;
    this.relation = relation;
    this.consanguine = consanguine;
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
