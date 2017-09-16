package site.kason.tempera.extension;

/**
 *
 * @author Kason Yang
 */
public interface Function {

  String getName();

  Class<?>[] getParameters();

  Object execute(Object[] arguments);

  Class<?> getReturnType();

}
