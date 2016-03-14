/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.ui;

import java.util.List;
import progettotlp.classes.DdT;
import java.io.File;
import java.net.URL;
import progettotlp.persistenza.AbstractPersistenza;
import progettotlp.persistenza.DdTManagerImpl;
import progettotlp.persistenza.AziendaManagerImpl;
import progettotlp.persistenza.AbstractTest;
import java.util.ArrayList;
import javax.swing.table.DefaultTableModel;
import org.junit.Before;
import javax.swing.JInternalFrame;
import javax.swing.JTable;
import org.junit.Test;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.NoSelectedRow;
import progettotlp.models.SceltaDdTTableModelUtils;
import static org.junit.Assert.*;

/**
 *
 * @author vincenzo
 */
public class FormSceltaDdTUtilitiesFunctionalTest extends AbstractTest{

    protected FormSceltaDdTUtilities form = new FormSceltaDdTUtilities(new JInternalFrame(), null, null);

    @Before
    public void init() throws Exception{
        JTable tabellaDdT=new JTable();
        tabellaDdT.setModel(SceltaDdTTableModelUtils.getDefaultTableModel());
        tabellaDdT.setName("tabellaDdT"); // NOI18N
        tabellaDdT.setRowHeight(22);
        tabellaDdT.getColumnModel().getColumn(0).setResizable(false);
        tabellaDdT.getColumnModel().getColumn(0).setHeaderValue("ID"); // NOI18N
        tabellaDdT.getColumnModel().getColumn(1).setResizable(false);
        tabellaDdT.getColumnModel().getColumn(1).setHeaderValue("Data"); // NOI18N
        tabellaDdT.getColumnModel().getColumn(2).setResizable(false);
        tabellaDdT.getColumnModel().getColumn(2).setHeaderValue("Cliente"); // NOI18N

        DefaultTableModel model = (DefaultTableModel)tabellaDdT.getModel();
        ArrayList<Object> x=new ArrayList<Object>();
        x.add(106);
        x.add("01/01/2012");
        x.add("azienda1");
        model.addRow(x.toArray());

        x=new ArrayList<Object>();
        x.add(107);
        x.add("01/01/2012");
        x.add("azienda2");
        model.addRow(x.toArray());

        x=new ArrayList<Object>();
        x.add(108);
        x.add("01/01/2012");
        x.add("azienda3");
        model.addRow(x.toArray());

        form.tabellaDdT=tabellaDdT;

        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareFormSceltaDdTTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
    }

    @Test
    public void testCancellaSelectedDdT() throws NoSelectedRow, GenericExceptionToPrint {
        JTable tabellaDdT = form.tabellaDdT;

        tabellaDdT.setRowSelectionInterval(1, 1);
        assertNotNull(retrieveObject(DdT.class, 2L,(DdTManagerImpl)form.ddtManager));
        form.cancellaSelectedDdT();
        assertNull(retrieveObject(DdT.class, 2L,(DdTManagerImpl)form.ddtManager));

        tabellaDdT.setRowSelectionInterval(0, 0);
        assertNotNull(retrieveObject(DdT.class, 1L,(DdTManagerImpl)form.ddtManager));
        form.cancellaSelectedDdT();
        assertNull(retrieveObject(DdT.class, 1L,(DdTManagerImpl)form.ddtManager));

        tabellaDdT.setRowSelectionInterval(0, 0);
        assertNotNull(retrieveObject(DdT.class, 3L,(DdTManagerImpl)form.ddtManager));
        form.cancellaSelectedDdT();
        assertNull(retrieveObject(DdT.class, 3L,(DdTManagerImpl)form.ddtManager));
    }

    @Test
    public void testCompilaListaSceltaDdT() throws Exception {
        form.compilaListaSceltaDdT();

        DefaultTableModel model = (DefaultTableModel)form.tabellaDdT.getModel();
        assertEquals(4, model.getRowCount());
        assertEquals(109, model.getValueAt(0, 0));
        assertEquals("04/05/2012", model.getValueAt(0, 1));
        assertEquals("azienda4", model.getValueAt(0, 2));
        assertEquals(108, model.getValueAt(1, 0));
        assertEquals("03/05/2012", model.getValueAt(1, 1));
        assertEquals("azienda3", model.getValueAt(1, 2));
        assertEquals(107, model.getValueAt(2, 0));
        assertEquals("02/05/2012", model.getValueAt(2, 1));
        assertEquals("azienda2", model.getValueAt(2, 2));
        assertEquals(106, model.getValueAt(3, 0));
        assertEquals("01/05/2012", model.getValueAt(3, 1));
        assertEquals("azienda1", model.getValueAt(3, 2));
    }

    @Override
    protected List<Class<? extends AbstractPersistenza>> getManagersClass() {
        List<Class<? extends AbstractPersistenza>> res = new ArrayList<Class<? extends AbstractPersistenza>>();
        res.add(AziendaManagerImpl.class);
        res.add(DdTManagerImpl.class);
        return res;
    }

    @Override
    protected Object getObjectToInitialize() {
        return form;
    }
}
