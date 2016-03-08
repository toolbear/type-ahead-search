package tas.directive;

import javax.inject.Inject;
import tas.*;

public class QuitDirective implements Directive {
  public final String name() {
    return "quit";
  }

  public DirectiveResult apply(String parameters) {
    return DirectiveResult.SHUTDOWN;
  }
}
