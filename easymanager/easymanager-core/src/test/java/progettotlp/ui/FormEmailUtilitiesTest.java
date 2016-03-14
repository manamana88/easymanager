/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import org.junit.Before;
import org.junit.Test;
import progettotlp.persistenza.AccountManager;
import progettotlp.persistenza.AziendaManager;
import progettotlp.persistenza.FatturaManager;
import static org.junit.Assert.*;

/**
 *
 * @author vincenzo
 */
public class FormEmailUtilitiesTest {

    FormEmailUtilities form;

    public FormEmailUtilitiesTest() {
        form= new FormEmailUtilities(new JDialog(),null, null, null, null);
    }

    @Before
    public void init(){
        List<Class<?extends Object>> ignore = new ArrayList<Class<? extends Object>>();
        ignore.add(AziendaManager.class);
        ignore.add(AccountManager.class);
        ignore.add(FatturaManager.class);
        UiTestUtilities.initializeClass(form, ignore);
    }

    @Test
    public void testChargeComboBox() {
    }

    @Test
    public void testExtractAziendaName() {
        assertEquals("my azienda", form.extractAziendaName("my azienda [ csaon wdqop ]"));
        assertEquals("Antica Sartoria s.r.l.",form.extractAziendaName("Antica Sartoria s.r.l. [antica@antica.it]"));
    }

    @Test
    public void testGetSelectedAziendaName() {
    }

    @Test
    public void testResetForm() {
    }

    @Test
    public void testChangeDestinatarioListener() {
    }

    @Test
    public void testShowForm() {
    }

    @Test
    public void testHideForm() {
    }

}