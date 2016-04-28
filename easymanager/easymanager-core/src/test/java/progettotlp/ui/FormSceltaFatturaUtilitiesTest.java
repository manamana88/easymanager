/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.ui;

import java.text.ParseException;

import javax.swing.table.DefaultTableModel;

import java.util.Arrays;
import java.util.Date;
import java.util.Properties;

import javax.swing.JInternalFrame;
import javax.swing.JTable;

import org.easymock.EasyMock;
import org.junit.Before;
import org.junit.Test;

import progettotlp.persistenza.FatturaManager;
import progettotlp.classes.Azienda;
import progettotlp.classes.Fattura;
import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.exceptions.toprint.CantRetrieveException;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.NoSelectedRow;
import progettotlp.facilities.ConfigurationManager;
import progettotlp.facilities.ConfigurationManager.Property;
import progettotlp.facilities.DateUtils;
import progettotlp.models.SceltaFattureTableModelUtils;
import progettotlp.test.AnnualTest;

/**
 *
 * @author vincenzo
 */
public class FormSceltaFatturaUtilitiesTest extends AnnualTest{
    
    FormSceltaFatturaUtilities form;
    
    @Before
    public void init(){
        JTable tabellaFatture=new JTable();
        tabellaFatture.setModel(SceltaFattureTableModelUtils.getDefaultTableModel());
        tabellaFatture.setName("tabellaFatture"); // NOI18N
        tabellaFatture.setColumnModel(SceltaFattureTableModelUtils.getDefaultTableColumnModel());

        Properties properties = new Properties();
        properties.put(Property.IVA_DEFAULT.getValue(), "21");
        ConfigurationManager.setProperties(properties);
        
        form=new FormSceltaFatturaUtilities(new JInternalFrame(), null, null);
        form.tabellaFatture=tabellaFatture;
    }
    
    @Test
    public void testResetSceltaFattura() {
        ((DefaultTableModel)form.tabellaFatture.getModel()).addRow(new Object[]{1,"01/01/2012","01/03/2012","azienda1"});
        assertEquals(1, form.tabellaFatture.getRowCount());
        form.resetSceltaFattura();
        assertEquals(0, form.tabellaFatture.getRowCount());
    }
    
    @Test
    public void testCompilaSceltaFattura() throws ParseException {
        FatturaManager f = EasyMock.createMock(FatturaManager.class);
        Azienda a1= new Azienda();
        a1.setNome("azienda1");
        Azienda a2= new Azienda();
        a2.setNome("azienda2");
        Date d1 = DateUtils.parseDate("01/01/2012");
        Date d2 = DateUtils.parseDate("01/03/2012");
        Date d3 = DateUtils.parseDate("01/04/2012");
        Date d4 = DateUtils.parseDate("01/06/2012");
        Fattura f1= new Fattura();
        f1.setId(1);
        f1.setEmissione(d1);
        f1.setScadenza(d2);
        f1.setCliente(a1);
        Fattura f2= new Fattura();
        f2.setId(2);
        f2.setEmissione(d3);
        f2.setScadenza(d4);
        f2.setCliente(a2);
        expect(f.getAllFatture(false,false)).andReturn(Arrays.asList(f1,f2));
        replay(f);
        
        form.fatturaManager=f;
        form.compilaSceltaFattura();
        
        verify(f);
        JTable t = form.tabellaFatture;
        assertEquals(4,t.getColumnCount());
        assertEquals("ID",t.getColumnName(0));
        assertEquals("Cliente",t.getColumnName(1));
        assertEquals("Emissione",t.getColumnName(2));
        assertEquals("Scadenza",t.getColumnName(3));
        assertEquals(2, t.getRowCount());
        assertEquals(1, t.getValueAt(0, 0));
        assertEquals("azienda1", t.getValueAt(0, 1));
        assertEquals(DateUtils.formatDate(d1), t.getValueAt(0, 2));
        assertEquals(DateUtils.formatDate(d2), t.getValueAt(0, 3));
        assertEquals(2, t.getValueAt(1, 0));
        assertEquals("azienda2", t.getValueAt(1, 1));
        assertEquals(DateUtils.formatDate(d3), t.getValueAt(1, 2));
        assertEquals(DateUtils.formatDate(d4), t.getValueAt(1, 3));
        
    }
    
    @Test
    public void testGetSelectedFatturaId() throws NoSelectedRow {
        DefaultTableModel model = (DefaultTableModel)form.tabellaFatture.getModel();
        model.addRow(new Object[]{1,"01/01/2012","01/03/2012","azienda1"});
        model.addRow(new Object[]{2,"01/01/2012","01/03/2012","azienda2"});
        model.addRow(new Object[]{3,"01/01/2012","01/03/2012","azienda3"});
        try{
            form.getSelectedFatturaId();
            fail();
        } catch (NoSelectedRow e){}
        form.tabellaFatture.setRowSelectionInterval(0, 0);
        assertEquals(1, form.getSelectedFatturaId());
        form.tabellaFatture.setRowSelectionInterval(1, 1);
        assertEquals(2, form.getSelectedFatturaId());
        form.tabellaFatture.setRowSelectionInterval(2, 2);
        assertEquals(3, form.getSelectedFatturaId());
    }

    @Test
    public void testGetSelectedFattura() throws NoSelectedRow, CantRetrieveException {
        DefaultTableModel model = (DefaultTableModel)form.tabellaFatture.getModel();
        model.addRow(new Object[]{1,"01/01/2012","01/03/2012","azienda1"});
        model.addRow(new Object[]{2,"01/01/2012","01/03/2012","azienda2"});
        model.addRow(new Object[]{3,"01/01/2012","01/03/2012","azienda3"});
        model.addRow(new Object[]{4,"01/01/2012","01/03/2012","azienda4"});
        FatturaManager f = EasyMock.createMock(FatturaManager.class);
        expect(f.getFattura(1,false,false)).andReturn(new Fattura());
        expect(f.getFattura(2,false,false)).andReturn(new Fattura());
        expect(f.getFattura(3,false,false)).andReturn(new Fattura());
        expect(f.getFattura(4,false,false)).andReturn(null);
        replay(f);
        
        form.fatturaManager=f;
        try{
            form.getSelectedFattura(false,false);
            fail();
        } catch (NoSelectedRow e){}
        form.tabellaFatture.setRowSelectionInterval(0, 0);
        form.getSelectedFattura(false,false);
        form.tabellaFatture.setRowSelectionInterval(1, 1);
        form.getSelectedFattura(false,false);
        form.tabellaFatture.setRowSelectionInterval(2, 2);
        form.getSelectedFattura(false,false);
        form.tabellaFatture.setRowSelectionInterval(3, 3);
        try{
            form.getSelectedFattura(false,false);
            fail();
        } catch (CantRetrieveException e){}
        
        verify(f);
    }

    @Test
    public void testCancellaFattura() throws NoSelectedRow, GenericExceptionToPrint, PersistenzaException {
        JTable tabellaFatture = form.tabellaFatture;
        DefaultTableModel model = (DefaultTableModel)tabellaFatture.getModel();
        model.addRow(new Object[]{1,"01/01/2012","01/03/2012","azienda1"});
        model.addRow(new Object[]{2,"01/01/2012","01/03/2012","azienda2"});
        model.addRow(new Object[]{3,"01/01/2012","01/03/2012","azienda3"});
        FatturaManager f = EasyMock.createMock(FatturaManager.class);
        f.cancellaFattura(1);
        f.cancellaFattura(3);
        expectLastCall().andThrow(new PersistenzaException());
        f.cancellaFattura(3);
        f.cancellaFattura(2);
        replay(f);
        
        form.fatturaManager=f;
        try{
            form.cancellaFattura();
            fail();
        } catch (NoSelectedRow e){}
        assertEquals(3, tabellaFatture.getRowCount());
        tabellaFatture.setRowSelectionInterval(0, 0);
        form.cancellaFattura();
        assertEquals(2, tabellaFatture.getRowCount());
        assertEquals(2, tabellaFatture.getValueAt(0, 0));
        assertEquals(3, tabellaFatture.getValueAt(1, 0));
        tabellaFatture.setRowSelectionInterval(1, 1);
        try{
            form.cancellaFattura();
            fail();
        } catch (GenericExceptionToPrint e){}
        assertEquals(2, tabellaFatture.getRowCount());
        form.cancellaFattura();
        assertEquals(1, tabellaFatture.getRowCount());
        assertEquals(2, tabellaFatture.getValueAt(0, 0));
        tabellaFatture.setRowSelectionInterval(0, 0);
        form.cancellaFattura();
        assertEquals(0, tabellaFatture.getRowCount());
        
        verify();
    }

    @Test
    public void testStampaFattura() {
    }

    @Test
    public void testShowForm() {
    }
}
