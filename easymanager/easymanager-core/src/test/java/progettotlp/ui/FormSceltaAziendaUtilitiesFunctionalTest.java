/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.ui;

import java.util.ArrayList;
import java.util.List;
import progettotlp.classes.Azienda;
import java.io.File;
import java.net.URL;
import org.junit.Before;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import org.junit.Test;
import progettotlp.persistenza.AbstractPersistenza;
import progettotlp.persistenza.AziendaManagerImpl;
import progettotlp.persistenza.DdTManagerImpl;
import progettotlp.persistenza.FatturaManagerImpl;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.NoSelectedRow;
import progettotlp.persistenza.AbstractTest;
import static org.junit.Assert.*;

/**
 *
 * @author vincenzo
 */
public class FormSceltaAziendaUtilitiesFunctionalTest extends AbstractTest{
    protected FormSceltaAziendaUtilities form;
    
    public FormSceltaAziendaUtilitiesFunctionalTest() {
        form=new FormSceltaAziendaUtilities(new JInternalFrame(), null, null, null);
        form.mese=new JTextField();
    }
    
    @Before
    public void init() throws Exception{
        JList listaAziende=new JList();
        DefaultListModel model = new DefaultListModel();
        model.addElement("azienda1");
        model.addElement("azienda2");
        model.addElement("azienda3");
        listaAziende.setModel(model);
        form.listaAziende=listaAziende;
    }

    @Test
    public void testCaricaLista() throws Exception{
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareFormSceltaAziendaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        form.caricaLista();
        
        assertEquals(3,form.listaAziende.getModel().getSize());
        assertEquals("azienda1",form.listaAziende.getModel().getElementAt(0));
        assertEquals("azienda2",form.listaAziende.getModel().getElementAt(1));
        assertEquals("azienda3",form.listaAziende.getModel().getElementAt(2));
    }
    
    @Test
    public void testCaricaListaPerEmissioneFattura() throws Exception{
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareFormSceltaAziendaTests_1.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);

        form.caricaListaPerEmissioneFattura(5, "Maggio");
        
        assertEquals(2, form.listaAziende.getModel().getSize());
        assertEquals("azienda3", form.listaAziende.getModel().getElementAt(0));
        assertEquals("azienda4", form.listaAziende.getModel().getElementAt(1));
    }

    @Test
    public void testCancellaAzienda() throws NoSelectedRow, GenericExceptionToPrint, Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareFormSceltaAziendaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);

        form.listaAziende.setSelectedIndex(1);
        assertNotNull(retrieveObject(Azienda.class, 3L,(AziendaManagerImpl)form.aziendaManager));
        form.cancellaAzienda();
        assertNull(retrieveObject(Azienda.class, 3L,(AziendaManagerImpl)form.aziendaManager));

        form.listaAziende.setSelectedIndex(0);
        assertNotNull(retrieveObject(Azienda.class, 2L,(AziendaManagerImpl)form.aziendaManager));
        form.cancellaAzienda();
        assertNull(retrieveObject(Azienda.class, 2L,(AziendaManagerImpl)form.aziendaManager));
        
        form.listaAziende.setSelectedIndex(0);
        assertNotNull(retrieveObject(Azienda.class, 4L,(AziendaManagerImpl)form.aziendaManager));
        form.cancellaAzienda();
        assertNull(retrieveObject(Azienda.class, 4L,(AziendaManagerImpl)form.aziendaManager));
    }

    @Override
    protected List<Class<? extends AbstractPersistenza>> getManagersClass() {
        List<Class<? extends AbstractPersistenza>> res = new ArrayList<Class<? extends AbstractPersistenza>>();
        res.add(AziendaManagerImpl.class);
        res.add(DdTManagerImpl.class);
        res.add(FatturaManagerImpl.class);
        return res;
    }

    @Override
    protected Object getObjectToInitialize() {
        return form;
    }

}
