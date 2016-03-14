/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.exceptions;

/**
 *
 * @author Vincenzo
 */
public class PrintException extends Exception {

    public PrintException(Throwable cause) {
        super(cause);
    }

    public PrintException(String message, Throwable cause) {
        super(message, cause);
    }

    public PrintException(String message) {
        super(message);
    }

    public PrintException() {
    }

    
}
