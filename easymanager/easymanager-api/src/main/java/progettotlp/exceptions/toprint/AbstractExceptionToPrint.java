/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.exceptions.toprint;

/**
 *
 * @author vincenzo
 */
public class AbstractExceptionToPrint extends Exception {

    String header;
    String body;
    
    public AbstractExceptionToPrint(String header,String body){
        super(body);
        this.body=body;
        this.header=header;
    }
    
    public AbstractExceptionToPrint(String header,String body, Throwable cause){
        super(body,cause);
        this.body=body;
        this.header=header;
    }
    
    public String getHeader() {
        return header;
    }

    public String getBody() {
        return body;
    }
    
}
