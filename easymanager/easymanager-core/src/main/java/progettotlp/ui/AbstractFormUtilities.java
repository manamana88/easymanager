/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.ui;

import java.awt.Container;
import java.text.ParseException;
import java.util.Date;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import progettotlp.exceptions.InitializationException;
import progettotlp.exceptions.toprint.ValidationException;
import progettotlp.facilities.DateUtils;

/**
 *
 * @author vincenzo
 */

public abstract class AbstractFormUtilities {
    private Logger logger = LoggerFactory.getLogger(AbstractFormUtilities.class);
    protected Container form;
    
    protected void init(Container form){
        this.form = form;
        try {
            GeneralUtilities.initializeClassFromJDialog(this, form);
            GeneralUtilities.areFieldsNull(this);
        } catch (InitializationException ex) {
            logger.warn(ex.getMessage());
        }
    }
    
    public void hideForm(){
        if (form instanceof JInternalFrame){
            ((JInternalFrame)form).dispose();
        } else if (form instanceof JDialog){
            ((JDialog)form).dispose();
        }
    }

    public Container getForm(){
        return form;
    }
    
    protected Integer parseIntegerValue(JTextField field) throws ValidationException{
        try{
            final String text = field.getText();
            if (text.trim().isEmpty()){
                return null;
            } else {
                return Integer.parseInt(text);
            }
        } catch (NumberFormatException e){
            throw new ValidationException("Dati errati nel campo: "+field.getName(),e);
        }
    }

    protected Integer parseIntegerValue(JTextField field, boolean mandatory) throws ValidationException{
        Integer parseIntegerValue = parseIntegerValue(field);
        if (mandatory && parseIntegerValue==null){
            throw new ValidationException("Dati errati nel campo: "+field.getName());
        }
        return parseIntegerValue;
    }

    protected Double parseDoubleValue(JTextField field) throws ValidationException{
        try{
            final String text = field.getText();
            if (text.trim().isEmpty()){
                return null;
            } else {
                return Double.parseDouble(text);
            }
        } catch (NumberFormatException e){
            throw new ValidationException("Dati errati nel campo: "+field.getName(),e);
        }
    }

    protected Double parseDoubleValue(JTextField field, boolean mandatory) throws ValidationException{
        Double parseDoubleValue = parseDoubleValue(field);
        if (mandatory && parseDoubleValue==null){
            throw new ValidationException("Dati errati nel campo: "+field.getName());
        }
        return parseDoubleValue;
    }

    protected Float parseFloatValue(JTextField field) throws ValidationException{
        try{
            final String text = field.getText();
            if (text.trim().isEmpty()){
                return null;
            } else {
                return Float.parseFloat(text);
            }
        } catch (NumberFormatException e){
            throw new ValidationException("Dati errati nel campo: "+field.getName(),e);
        }
    }

    protected Float parseFloatValue(JTextField field, boolean mandatory) throws ValidationException{
        Float parseFloatValue = parseFloatValue(field);
        if (mandatory && parseFloatValue==null){
            throw new ValidationException("Dati errati nel campo: "+field.getName());
        }
        return parseFloatValue;
    }

    protected Date parseDateValue(JTextField field, boolean mandatory) throws ValidationException{
        String text = field.getText();
        if (mandatory && text.isEmpty()){
            throw new ValidationException("Il campo "+field.getName()+" è obbligatorio");
        }
        try {
            return DateUtils.parseDate(text);
        } catch (ParseException ex) {
            throw new ValidationException("Dati errati nel campo: "+field.getName()+
                    "."+'\n'+"Inserire una stringa del tipo gg/mm/yyyy",ex);
        }
    }

    protected void setWithNumber(JTextField field, Number n){
        if (n==null || n.doubleValue()==0){
            field.setText(null);
        } else {
            field.setText(n.toString());
        }
    }
}
