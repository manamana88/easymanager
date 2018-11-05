/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.exceptions.toprint;

/**
 *
 * @author vincenzo
 */
public class GenericExceptionToPrint extends AbstractExceptionToPrint{

    public GenericExceptionToPrint(String header, String body) {
        super(header, body);
    }

    public GenericExceptionToPrint(String header, String body, Throwable cause) {
        super(header, body, cause);
    }
}
