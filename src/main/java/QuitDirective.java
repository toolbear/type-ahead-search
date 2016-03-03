import javax.inject.Inject;

class QuitDirective implements Directive {
  public final String name() {
    return "quit";
  }

  public DirectiveResult apply(String parameters) {
    return DirectiveResult.SHUTDOWN;
  }
}
