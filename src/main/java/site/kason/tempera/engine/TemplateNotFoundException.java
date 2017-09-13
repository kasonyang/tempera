package site.kason.tempera.engine;

import java.io.IOException;

/**
 *
 * @author Kason Yang
 */
public class TemplateNotFoundException extends IOException {

    public TemplateNotFoundException() {
    }

    public TemplateNotFoundException(String message) {
        super(message);
    }

    public TemplateNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

    public TemplateNotFoundException(Throwable cause) {
        super(cause);
    }

}
