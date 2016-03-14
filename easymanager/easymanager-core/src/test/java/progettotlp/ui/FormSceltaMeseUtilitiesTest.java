/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.ui;

import progettotlp.test.AnnualTest;
import java.util.logging.Level;
import java.util.logging.Logger;
import progettotlp.facilities.DateUtils;
import java.util.Date;
import java.text.ParseException;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author vincenzo
 */
public class FormSceltaMeseUtilitiesTest extends AnnualTest{
    
    FormSceltaMeseUtilities form = new FormSceltaMeseUtilities(new JInternalFrame());
    
    @Before
    public void init() {
        form.elencoMesi=new JComboBox();
        Date x=null;
        try {
            x = DateUtils.parseDate("01/05/2012");
        } catch (ParseException ex) {
            Logger.getLogger(FormSceltaMeseUtilitiesTest.class.getName()).log(Level.SEVERE, null, ex);
        }
        form.elencoMesi.removeAllItems();
        form.elencoMesi.addItem(DateUtils.parseMonthInt(DateUtils.getMonth(x)-1));
        form.elencoMesi.addItem(DateUtils.getMonthString(x));
        form.elencoMesi.addItem(DateUtils.parseMonthInt(DateUtils.getMonth(x)+1));
        form.elencoMesi.setSelectedIndex(0);
    }

    @Test
    public void testGetSelectedMeseInt() {
        form.elencoMesi.setSelectedIndex(0);
        assertEquals(4,form.getSelectedMeseInt());
        form.elencoMesi.setSelectedIndex(1);
        assertEquals(5,form.getSelectedMeseInt());
        form.elencoMesi.setSelectedIndex(2);
        assertEquals(6,form.getSelectedMeseInt());
    }

    @Test
    public void testGetSelectedMeseString() {
        form.elencoMesi.setSelectedIndex(0);
        assertEquals("Aprile",form.getSelectedMeseString());
        form.elencoMesi.setSelectedIndex(1);
        assertEquals("Maggio",form.getSelectedMeseString());
        form.elencoMesi.setSelectedIndex(2);
        assertEquals("Giugno",form.getSelectedMeseString());
    }

    @Test
    public void testShowForm() throws ParseException {
        form.showForm();
        assertEquals(3,form.elencoMesi.getItemCount());
        GregorianCalendar g = new GregorianCalendar();
        assertEquals(DateUtils.parseMonthInt(g.get(Calendar.MONTH)), form.elencoMesi.getItemAt(0));
        assertEquals(DateUtils.parseMonthInt(g.get(Calendar.MONTH) +1 ), form.elencoMesi.getItemAt(1));
        assertEquals(DateUtils.parseMonthInt(g.get(Calendar.MONTH) +2 ), form.elencoMesi.getItemAt(2));
    }
}
