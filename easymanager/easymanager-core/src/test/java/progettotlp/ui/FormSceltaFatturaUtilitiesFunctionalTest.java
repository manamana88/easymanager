/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.ui;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.DefaultTableModel;
import javax.swing.JInternalFrame;
import javax.swing.JTable;
import org.junit.Before;
import org.junit.Test;
import progettotlp.classes.Fattura;
import static org.junit.Assert.*;
import progettotlp.persistenza.AbstractPersistenza;
import progettotlp.persistenza.FatturaManagerImpl;
import progettotlp.exceptions.toprint.CantRetrieveException;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.NoSelectedRow;
import progettotlp.facilities.DateUtils;
import progettotlp.models.SceltaFattureTableModelUtils;
import progettotlp.persistenza.AbstractTest;

/**
 *
 * @author vincenzo
 */
public class FormSceltaFatturaUtilitiesFunctionalTest extends AbstractTest{
    
    protected FormSceltaFatturaUtilities form;

    public FormSceltaFatturaUtilitiesFunctionalTest() {
        form=new FormSceltaFatturaUtilities(new JInternalFrame(), null, null);
    }
    
    @Before
    public void init() throws Exception{
        JTable tabellaFatture=new JTable();
        tabellaFatture.setModel(SceltaFattureTableModelUtils.getDefaultTableModel());
        tabellaFatture.setName("tabellaFatture"); // NOI18N
        tabellaFatture.setColumnModel(SceltaFattureTableModelUtils.getDefaultTableColumnModel());

        form.tabellaFatture=tabellaFatture;

        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareFormSceltaFatturaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
    }
    
    @Test
    public void testCompilaSceltaFattura() throws Exception{
        form.compilaSceltaFattura();
        
        JTable t = form.tabellaFatture;
        assertEquals(4,t.getColumnCount());
        assertEquals("ID",t.getColumnName(0));
        assertEquals("Cliente",t.getColumnName(1));
        assertEquals("Emissione",t.getColumnName(2));
        assertEquals("Scadenza",t.getColumnName(3));
        assertEquals(2, t.getRowCount());
        assertEquals(2, t.getValueAt(0, 0));
        assertEquals("azienda2", t.getValueAt(0, 1));
        assertEquals("01/04/2012", t.getValueAt(0, 2));
        assertEquals("01/06/2012", t.getValueAt(0, 3));
        assertEquals(1, t.getValueAt(1, 0));
        assertEquals("azienda1", t.getValueAt(1, 1));
        assertEquals("01/01/2012", t.getValueAt(1, 2));
        assertEquals("01/03/2012", t.getValueAt(1, 3));
        
    }

    @Test
    public void testGetSelectedFattura() throws NoSelectedRow, CantRetrieveException, Exception{
        DefaultTableModel model = (DefaultTableModel)form.tabellaFatture.getModel();
        model.addRow(new Object[]{1,"01/01/2012","01/03/2012","azienda1"});
        model.addRow(new Object[]{2,"01/04/2012","01/06/2012","azienda2"});
        model.addRow(new Object[]{4,"01/01/2012","01/03/2012","azienda4"});
        
        Fattura retrieved=null;
        try{
            form.getSelectedFattura(false,false);
            fail();
        } catch (NoSelectedRow e){}
        form.tabellaFatture.setRowSelectionInterval(0, 0);
        retrieved=form.getSelectedFattura(false,false);
        assertEquals(retrieved.getId(), new Integer(1));
        assertEquals(DateUtils.formatDate(retrieved.getEmissione()), "01/01/2012");
        assertEquals(DateUtils.formatDate(retrieved.getScadenza()), "01/03/2012");
        assertEquals(retrieved.getCliente().getNome(), "azienda1");
        form.tabellaFatture.setRowSelectionInterval(1, 1);
        retrieved=form.getSelectedFattura(false,false);
        assertEquals(retrieved.getId(), new Integer(2));
        assertEquals(DateUtils.formatDate(retrieved.getEmissione()), "01/04/2012");
        assertEquals(DateUtils.formatDate(retrieved.getScadenza()), "01/06/2012");
        assertEquals(retrieved.getCliente().getNome(), "azienda2");
        form.tabellaFatture.setRowSelectionInterval(2, 2);
        try{
            form.getSelectedFattura(false,false);
            fail();
        } catch (CantRetrieveException e){}
        
    }

    @Test
    public void testCancellaFattura() throws NoSelectedRow, GenericExceptionToPrint {
        JTable tabellaFatture = form.tabellaFatture;
        DefaultTableModel model = (DefaultTableModel)tabellaFatture.getModel();
        model.addRow(new Object[]{1,"01/01/2012","01/03/2012","azienda1"});
        model.addRow(new Object[]{2,"01/01/2012","01/03/2012","azienda2"});
        model.addRow(new Object[]{3,"01/01/2012","01/03/2012","azienda3"});
        
        tabellaFatture.setRowSelectionInterval(0, 0);
        assertNotNull(retrieveObject(Fattura.class, 1L,(FatturaManagerImpl)form.fatturaManager));
        form.cancellaFattura();
        assertNull(retrieveObject(Fattura.class, 1L,(FatturaManagerImpl)form.fatturaManager));

        tabellaFatture.setRowSelectionInterval(1, 1);
        try{
            form.cancellaFattura();
            fail();
        } catch (GenericExceptionToPrint e){}

        tabellaFatture.setRowSelectionInterval(0, 0);
        assertNotNull(retrieveObject(Fattura.class, 2L,(FatturaManagerImpl)form.fatturaManager));
        form.cancellaFattura();
        assertNull(retrieveObject(Fattura.class, 2L,(FatturaManagerImpl)form.fatturaManager));
    }

    @Override
    protected List<Class<? extends AbstractPersistenza>> getManagersClass() {
        List<Class<? extends AbstractPersistenza>> res = new ArrayList<Class<? extends AbstractPersistenza>>();
        res.add(FatturaManagerImpl.class);
        return res;
    }

    @Override
    protected Object getObjectToInitialize() {
        return form;
    }
}
