/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.ui;

import progettotlp.test.AnnualTest;
import progettotlp.facilities.DateUtils;
import java.util.Date;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.classes.Azienda;
import java.util.ArrayList;
import java.util.Arrays;
import javax.swing.table.DefaultTableModel;
import org.junit.Before;
import javax.swing.JInternalFrame;
import javax.swing.JTable;
import org.easymock.EasyMock;
import org.junit.Test;
import progettotlp.persistenza.DdTManager;
import progettotlp.classes.DdT;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.NoSelectedRow;
import progettotlp.models.SceltaDdTTableModelUtils;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

/**
 *
 * @author vincenzo
 */
public class FormSceltaDdTUtilitiesTest extends AnnualTest{
    
    FormSceltaDdTUtilities form = new FormSceltaDdTUtilities(new JInternalFrame(), null, null);
    
    @Before
    public void init(){
        JTable tabellaDdT=new JTable();
        tabellaDdT.setModel(SceltaDdTTableModelUtils.getDefaultTableModel());
        tabellaDdT.setName("tabellaDdT"); // NOI18N
        tabellaDdT.setRowHeight(22);
        tabellaDdT.setColumnModel(SceltaDdTTableModelUtils.getDefaultTableColumnModel());
        
        DefaultTableModel model = (DefaultTableModel)tabellaDdT.getModel();
        ArrayList<Object> x=new ArrayList<Object>();
        x.add(1);
        x.add("01/01/2012");
        x.add("azienda1");
        model.addRow(x.toArray());
        
        x=new ArrayList<Object>();
        x.add(2);
        x.add("01/01/2012");
        x.add("azienda2");
        model.addRow(x.toArray());
        
        x=new ArrayList<Object>();
        x.add(3);
        x.add("01/01/2012");
        x.add("azienda3");
        model.addRow(x.toArray());
        
        form.tabellaDdT=tabellaDdT;
    }

    @Test
    public void testRemoveSelectedRow() throws NoSelectedRow {
        JTable tabellaDdT = form.tabellaDdT;
        DefaultTableModel model = (DefaultTableModel)tabellaDdT.getModel();
        try{
            form.removeSelectedRow();
            fail();
        } catch (NoSelectedRow e){}
        assertEquals(3, model.getRowCount());
        
        tabellaDdT.setRowSelectionInterval(1, 1);
        form.removeSelectedRow();
        
        assertEquals(2, model.getRowCount());
        assertEquals(1, model.getValueAt(0, 0));
        assertEquals("01/01/2012", model.getValueAt(0, 1));
        assertEquals("azienda1", model.getValueAt(0, 2));
        assertEquals(3, model.getValueAt(1, 0));
        assertEquals("01/01/2012", model.getValueAt(1, 1));
        assertEquals("azienda3", model.getValueAt(1, 2));
        
        tabellaDdT.setRowSelectionInterval(0, 0);
        form.removeSelectedRow();
        
        assertEquals(1, model.getRowCount());
        assertEquals(3, model.getValueAt(0, 0));
        assertEquals("01/01/2012", model.getValueAt(0, 1));
        assertEquals("azienda3", model.getValueAt(0, 2));
        
        tabellaDdT.setRowSelectionInterval(0, 0);
        form.removeSelectedRow();
        
        assertEquals(0, model.getRowCount());
    }

    @Test
    public void testGetSelectedDdTId() throws NoSelectedRow {
        JTable tabellaDdT = form.tabellaDdT;
        try{
            form.getSelectedDdTId();
            fail();
        } catch (NoSelectedRow e){}
        
        tabellaDdT.setRowSelectionInterval(1, 1);
        assertEquals(2, form.getSelectedDdTId().intValue());
        
        tabellaDdT.setRowSelectionInterval(0, 0);
        assertEquals(1, form.getSelectedDdTId().intValue());
        
        tabellaDdT.setRowSelectionInterval(2, 2);
        assertEquals(3, form.getSelectedDdTId().intValue());
    }

    @Test
    public void testCancellaSelectedDdT() throws NoSelectedRow, GenericExceptionToPrint, PersistenzaException {
        DdTManager d = EasyMock.createMock(DdTManager.class);
        d.cancellaDdT(2);
        d.cancellaDdT(1);
        d.cancellaDdT(3);
        expectLastCall().andThrow(new PersistenzaException());
        d.cancellaDdT(3);
        replay(d);
        form.ddtManager=d;
        JTable tabellaDdT = form.tabellaDdT;
        DefaultTableModel model = (DefaultTableModel)tabellaDdT.getModel();
        try{
            form.cancellaSelectedDdT();
            fail();
        } catch (NoSelectedRow e){}
        assertEquals(3, model.getRowCount());
        
        tabellaDdT.setRowSelectionInterval(1, 1);
        form.cancellaSelectedDdT();
        
        assertEquals(2, model.getRowCount());
        assertEquals(1, model.getValueAt(0, 0));
        assertEquals("01/01/2012", model.getValueAt(0, 1));
        assertEquals("azienda1", model.getValueAt(0, 2));
        assertEquals(3, model.getValueAt(1, 0));
        assertEquals("01/01/2012", model.getValueAt(1, 1));
        assertEquals("azienda3", model.getValueAt(1, 2));
        
        tabellaDdT.setRowSelectionInterval(0, 0);
        form.cancellaSelectedDdT();
        
        assertEquals(1, model.getRowCount());
        assertEquals(3, model.getValueAt(0, 0));
        assertEquals("01/01/2012", model.getValueAt(0, 1));
        assertEquals("azienda3", model.getValueAt(0, 2));
        
        tabellaDdT.setRowSelectionInterval(0, 0);
        try{
            form.cancellaSelectedDdT();
            fail();
        } catch (GenericExceptionToPrint e){}
        
        assertEquals(1, model.getRowCount());
        assertEquals(3, model.getValueAt(0, 0));
        assertEquals("01/01/2012", model.getValueAt(0, 1));
        assertEquals("azienda3", model.getValueAt(0, 2));
        
        tabellaDdT.setRowSelectionInterval(0, 0);
        form.cancellaSelectedDdT();
        assertEquals(0, model.getRowCount());
        
        verify(d);
    }

    @Test
    public void testResetSceltaDdT() {
        form.resetSceltaDdT();
        DefaultTableModel model = (DefaultTableModel)form.tabellaDdT.getModel();
        assertEquals(0, model.getRowCount());
    }

    @Test
    public void testCompilaListaSceltaDdT() throws Exception {
        Azienda a1= new Azienda();
        a1.setNome("azienda1");
        Azienda a2= new Azienda();
        a2.setNome("azienda2");
        Azienda a3= new Azienda();
        a3.setNome("azienda3");
        Date data1=DateUtils.parseDate("01/01/2012");
        DdT d1= new DdT();
        d1.setData(data1);
        d1.setCliente(a1);
        d1.setId(1);
        DdT d2= new DdT();
        d2.setData(data1);
        d2.setCliente(a2);
        d2.setId(2);
        DdT d3= new DdT();
        d3.setData(data1);
        d3.setCliente(a3);
        d3.setId(3);
        DdTManager d = EasyMock.createMock(DdTManager.class);
        expect(d.getAllDdT(false,false)).andReturn(Arrays.asList(d1,d2,d3));
        replay(d);
        
        form.ddtManager=d;
        form.compilaListaSceltaDdT();
        
        
        DefaultTableModel model = (DefaultTableModel)form.tabellaDdT.getModel();
        assertEquals(3, model.getRowCount());
        assertEquals(1, model.getValueAt(0, 0));
        assertEquals("01/01/2012", model.getValueAt(0, 1));
        assertEquals("azienda1", model.getValueAt(0, 2));
        assertEquals(2, model.getValueAt(1, 0));
        assertEquals("01/01/2012", model.getValueAt(1, 1));
        assertEquals("azienda2", model.getValueAt(1, 2));
        assertEquals(3, model.getValueAt(2, 0));
        assertEquals("01/01/2012", model.getValueAt(2, 1));
        assertEquals("azienda3", model.getValueAt(2, 2));
        
        verify(d);
        
    }

}
