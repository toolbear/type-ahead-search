package tas.directive;

public interface Directive {
  String name();
  DirectiveResult apply(String parameters);
}
