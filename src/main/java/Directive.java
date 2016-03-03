import java.io.PrintWriter;

interface Directive {
  String name();
  void apply(String parameters, PrintWriter out, PrintWriter err);
}
