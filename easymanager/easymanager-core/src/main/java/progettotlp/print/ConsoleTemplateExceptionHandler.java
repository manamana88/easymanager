/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.print;

import freemarker.core.Environment;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import java.io.Writer;

/**
 *
 * @author Vincenzo
 */
public class ConsoleTemplateExceptionHandler implements TemplateExceptionHandler{

    public void handleTemplateException(TemplateException te, Environment e, Writer writer) throws TemplateException {
        te.getCauseException().printStackTrace();
    }
    
}
