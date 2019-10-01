/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.exceptions;

/**
 *
 * @author vincenzo
 */
public class InitializationException extends Exception {

    /**
     * Constructs an instance of <code>InitializationException</code> with the specified detail message.
     * @param msg the detail message.
     */
    public InitializationException(String msg) {
        super(msg);
    }

    public InitializationException(String message, Throwable cause) {
        super(message, cause);
    }
    
}
