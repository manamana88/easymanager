/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.ui;

import java.util.Date;
import org.junit.Before;
import javax.swing.JComboBox;
import javax.swing.JInternalFrame;
import org.junit.Test;
import progettotlp.ProgettoTLPView;
import progettotlp.facilities.DateUtils;
import progettotlp.test.AnnualTest;
import static org.junit.Assert.*;

/**
 *
 * @author vincenzo
 */
public class FormSceltaAnnoUtilitiesTest {

    FormSceltaAnnoUtilities form = new FormSceltaAnnoUtilities(new JInternalFrame());

    public FormSceltaAnnoUtilitiesTest() {
        form.anno=new JComboBox();
    }

    @Before
    public void init() throws Exception {
    	System.clearProperty(ProgettoTLPView.CURRENT_YEAR_PROPERTY);
        JComboBox anno = form.anno;
        anno.removeAllItems();
        anno.addItem(2012);
        anno.addItem(2011);
        anno.addItem(2010);
    }
    
    @Test
    public void testGetSelectedAnnoInt() {
        assertEquals(2012,form.getSelectedAnnoInt());
        form.anno.setSelectedIndex(1);
        assertEquals(2011,form.getSelectedAnnoInt());
        form.anno.setSelectedIndex(2);
        assertEquals(2010,form.getSelectedAnnoInt());
        form.anno.setSelectedItem(2012);
        assertEquals(2012,form.getSelectedAnnoInt());
    }

    @Test
    public void testCompilaFormAnno() {
        form.compilaFormAnno();
        JComboBox anno = form.anno;
        int currentYear = DateUtils.getYear(new Date());
        assertEquals(currentYear, anno.getSelectedItem());
        int itemCount = anno.getItemCount();
        assertEquals(5, itemCount);
        for (int i=0;i<itemCount;i++){
            assertEquals(i+currentYear-4, anno.getItemAt(i));
        }
    }

    @Test
    public void testSetAnno() {
    	System.clearProperty(ProgettoTLPView.CURRENT_YEAR_PROPERTY);
        assertNull(System.getProperty(ProgettoTLPView.CURRENT_YEAR_PROPERTY));
        form.anno.setSelectedItem(2011);
        form.setAnno();
        assertEquals("2011",System.getProperty(ProgettoTLPView.CURRENT_YEAR_PROPERTY));
    }

}