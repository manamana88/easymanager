/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.ui;

import progettotlp.exceptions.toprint.ValidationException;
import javax.swing.JTextField;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vincenzo
 */
public class AbstractFormUtilitiesTest {
    
    AbstractFormUtilities form;
    
    public AbstractFormUtilitiesTest() {
        form=new AbstractFormUtilitiesImpl();
    }

    @Test
    public void testInit() {
    }

    @Test
    public void testHideForm() {
    }

    @Test
    public void testParseIntegerValue() throws Exception {
        JTextField field=new JTextField();

        field.setText(null);
        assertNull(form.parseIntegerValue(field));

        field.setText("");
        assertNull(form.parseIntegerValue(field));

        field.setText("sbagliato");
        try{
            form.parseIntegerValue(field);
            fail();
        } catch (ValidationException e){}
        
        field.setText("3");
        assertEquals(new Integer(3), form.parseIntegerValue(field));
    }

    @Test
    public void testParseDoubleValue() throws Exception {
        JTextField field=new JTextField();

        field.setText(null);
        assertNull(form.parseDoubleValue(field));

        field.setText("");
        assertNull(form.parseDoubleValue(field));

        field.setText("sbagliato");
        try{
            form.parseDoubleValue(field);
            fail();
        } catch (ValidationException e){}

        field.setText("3");
        assertEquals(new Double(3), form.parseDoubleValue(field));

        field.setText("3.2");
        assertEquals(new Double(3.2), form.parseDoubleValue(field));

        field.setText("3,2");
        try{
            form.parseDoubleValue(field);
            fail();
        } catch (ValidationException e){}
    }

    @Test
    public void testParseFloatValue() throws Exception {
        JTextField field=new JTextField();

        field.setText(null);
        assertNull(form.parseFloatValue(field));

        field.setText("");
        assertNull(form.parseFloatValue(field));

        field.setText("sbagliato");
        try{
            form.parseFloatValue(field);
            fail();
        } catch (ValidationException e){}

        field.setText("3");
        assertEquals(new Float(3), form.parseFloatValue(field));

        field.setText("3.2");
        assertEquals(new Float(3.2), form.parseFloatValue(field));

        field.setText("3,2");
        try{
            form.parseFloatValue(field);
            fail();
        } catch (ValidationException e){}
    }

    @Test
    public void testSetWithDouble() {
        JTextField field = new JTextField();

        Double d=null;
        form.setWithNumber(field, d);
        assertEquals("",field.getText());

        d=new Double(0);
        form.setWithNumber(field, d);
        assertEquals("",field.getText());

        d=0D;
        form.setWithNumber(field, d);
        assertEquals("",field.getText());

        d=0.3;
        form.setWithNumber(field, d);
        assertEquals("0.3",field.getText());

        Integer i= new Integer(0);
        form.setWithNumber(field, i);
        assertEquals("",field.getText());

        i= 0;
        form.setWithNumber(field, i);
        assertEquals("",field.getText());

        i=3;
        form.setWithNumber(field, i);
        assertEquals("3",field.getText());

        Float f=new Float(0);
        form.setWithNumber(field, f);
        assertEquals("",field.getText());

        f=0F;
        form.setWithNumber(field, f);
        assertEquals("",field.getText());

        f=0.3F;
        form.setWithNumber(field, f);
        assertEquals("0.3",field.getText());
    }

    public class AbstractFormUtilitiesImpl extends AbstractFormUtilities {
    }

}