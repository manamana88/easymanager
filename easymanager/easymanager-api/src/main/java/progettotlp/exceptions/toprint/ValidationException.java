/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.exceptions.toprint;

/**
 *
 * @author vincenzo
 */
public class ValidationException extends AbstractExceptionToPrint{
    private static String DEFAULT_HEADER="Dati errati";

    public ValidationException(String header, String body) {
        super(header, body);
    }

    public ValidationException(String header, String body, Throwable cause) {
        super(header, body, cause);
    }

    public ValidationException(String body, Throwable cause) {
        super(DEFAULT_HEADER, body, cause);
    }

    public ValidationException(String body) {
        super(DEFAULT_HEADER, body);
    }

}
