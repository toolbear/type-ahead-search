interface Directive {
  String name();
  DirectiveResult apply(String parameters);
}

enum DirectiveResult { CONTINUE, SHUTDOWN }
