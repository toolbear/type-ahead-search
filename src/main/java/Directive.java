import java.io.PrintWriter;

interface Directive {
  String name();
  DirectiveResult apply(String parameters, PrintWriter out, PrintWriter err);
}

enum DirectiveResult { CONTINUE, SHUTDOWN }
