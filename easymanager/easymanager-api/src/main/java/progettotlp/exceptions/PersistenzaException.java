/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.exceptions;

/**
 *
 * @author vincenzo
 */
public class PersistenzaException extends Exception {
    
    public PersistenzaException(String message, Throwable cause) {
        super(message, cause);
    }

    public PersistenzaException(String message) {
        super(message);
    }

    public PersistenzaException() {
    }

}
