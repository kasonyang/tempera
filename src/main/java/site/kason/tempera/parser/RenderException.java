package site.kason.tempera.parser;

/**
 *
 * @author Kason Yang
 */
public class RenderException extends RuntimeException {

  public RenderException() {
  }

  public RenderException(String message) {
    super(message);
  }

  public RenderException(String message, Throwable cause) {
    super(message, cause);
  }

  public RenderException(Throwable cause) {
    super(cause);
  }

}
