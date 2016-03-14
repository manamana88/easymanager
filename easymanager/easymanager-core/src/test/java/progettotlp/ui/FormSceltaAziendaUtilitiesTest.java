/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package progettotlp.ui;

import progettotlp.test.AnnualTest;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.persistenza.FatturaManager;
import java.util.Arrays;
import org.junit.Before;
import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JInternalFrame;
import javax.swing.JTextField;
import org.easymock.EasyMock;
import org.junit.Test;
import progettotlp.persistenza.AziendaManager;
import progettotlp.persistenza.DdTManager;
import progettotlp.classes.Azienda;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.NoSelectedRow;
import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

/**
 *
 * @author vincenzo
 */
public class FormSceltaAziendaUtilitiesTest extends AnnualTest{
    FormSceltaAziendaUtilities form;
    
    public FormSceltaAziendaUtilitiesTest() {
        form=new FormSceltaAziendaUtilities(new JInternalFrame(), null, null, null);
        form.mese=new JTextField();
    }
    
    @Before
    public void init(){
        JList listaAziende=new JList();
        DefaultListModel model = new DefaultListModel();
        model.addElement("azienda1");
        model.addElement("azienda2");
        model.addElement("azienda3");
        listaAziende.setModel(model);
        form.listaAziende=listaAziende;
    }

    @Test
    public void testCaricaLista() {
        AziendaManager a = EasyMock.createMock(AziendaManager.class);
        Azienda azienda = new Azienda();
        azienda.setNome("azienda1");
        Azienda azienda2 = new Azienda();
        azienda2.setNome("azienda2");
        Azienda azienda3 = new Azienda();
        azienda3.setNome("azienda3");
        expect(a.getAziendeNonPrincipali()).andReturn(Arrays.asList(azienda,azienda2,azienda3));
        replay(a);
        
        form.aziendaManager=a;
        form.caricaLista();
        
        assertEquals(3,form.listaAziende.getModel().getSize());
        assertEquals("azienda1",form.listaAziende.getModel().getElementAt(0));
        assertEquals("azienda2",form.listaAziende.getModel().getElementAt(1));
        assertEquals("azienda3",form.listaAziende.getModel().getElementAt(2));
        
        verify(a);
    }
    
    @Test
    public void testCaricaListaPerEmissioneFattura() {
        AziendaManager a = EasyMock.createMock(AziendaManager.class);
        DdTManager d = EasyMock.createMock(DdTManager.class);
        FatturaManager f = EasyMock.createMock(FatturaManager.class);
        
        Azienda azienda1 = new Azienda();
        Azienda azienda2 = new Azienda();
        Azienda azienda3 = new Azienda();
        azienda3.setNome("azienda3");
        Azienda azienda4 = new Azienda();
        azienda4.setNome("azienda4");
        
        int mese = 5;
        expect(a.getAziendeNonPrincipali()).andReturn(Arrays.asList(azienda1,azienda2,azienda3,azienda4));
        expect(d.isEmptyDdTListMese(mese, azienda1)).andReturn(Boolean.TRUE);
        expect(d.isEmptyDdTListMese(mese, azienda2)).andReturn(Boolean.FALSE);
        expect(d.isEmptyDdTListMese(mese, azienda3)).andReturn(Boolean.FALSE);
        expect(d.isEmptyDdTListMese(mese, azienda4)).andReturn(Boolean.FALSE);
        expect(f.existsFattura(mese, azienda2)).andReturn(Boolean.TRUE);
        expect(f.existsFattura(mese, azienda3)).andReturn(Boolean.FALSE);
        expect(f.existsFattura(mese, azienda4)).andReturn(Boolean.FALSE);
        replay(a);
        replay(d);
        replay(f);
        
        form.aziendaManager=a;
        form.ddTManager=d;
        form.fatturaManager=f;
        
        form.caricaListaPerEmissioneFattura(mese, "Maggio");
        
        verify(a);
        verify(d);
        verify(f);
        
        assertEquals(2, form.listaAziende.getModel().getSize());
        assertEquals("azienda3", form.listaAziende.getModel().getElementAt(0));
        assertEquals("azienda4", form.listaAziende.getModel().getElementAt(1));
        assertEquals("Maggio", form.mese.getText());
        assertEquals(-1, form.listaAziende.getSelectedIndex());
        
    }

    @Test
    public void testCancellaAzienda() throws NoSelectedRow, GenericExceptionToPrint, PersistenzaException {
        AziendaManager a = EasyMock.createMock(AziendaManager.class);
        Azienda azienda = new Azienda();
        Azienda azienda2 = new Azienda();
        Azienda azienda3 = new Azienda();
        expect(a.getAziendaPerNome("azienda2")).andReturn(azienda);
        a.cancellaAzienda(azienda);
        expect(a.getAziendaPerNome("azienda1")).andReturn(azienda2);
        a.cancellaAzienda(azienda2);
        expect(a.getAziendaPerNome("azienda3")).andReturn(azienda3);
        a.cancellaAzienda(azienda3);
        expectLastCall().andThrow(new PersistenzaException());
        expect(a.getAziendaPerNome("azienda3")).andReturn(azienda3);
        a.cancellaAzienda(azienda3);
        replay(a);
        
        form.aziendaManager=a;
        try{
            form.cancellaAzienda();
            fail();
        } catch (NoSelectedRow e){}
        form.listaAziende.setSelectedIndex(1);
        form.cancellaAzienda();
        assertEquals(2,form.listaAziende.getModel().getSize());
        assertEquals("azienda1",form.listaAziende.getModel().getElementAt(0));
        assertEquals("azienda3",form.listaAziende.getModel().getElementAt(1));
        form.listaAziende.setSelectedIndex(0);
        form.cancellaAzienda();
        assertEquals(1,form.listaAziende.getModel().getSize());
        assertEquals("azienda3",form.listaAziende.getModel().getElementAt(0));
        form.listaAziende.setSelectedIndex(0);
        try{
            form.cancellaAzienda();
            fail();
        } catch (GenericExceptionToPrint e){}
        assertEquals(1,form.listaAziende.getModel().getSize());
        assertEquals("azienda3",form.listaAziende.getModel().getElementAt(0));
        form.listaAziende.setSelectedIndex(0);
        form.cancellaAzienda();
        assertEquals(0,form.listaAziende.getModel().getSize());
        
        verify(a);
    }

    @Test
    public void testShowForm() {
    }

    @Test
    public void testGetSelectedAzienda() throws NoSelectedRow {
        try{
            form.getSelectedAzienda();
            fail();
        } catch (NoSelectedRow e){}
        form.listaAziende.setSelectedIndex(0);
        assertEquals("azienda1", form.getSelectedAzienda());
        form.listaAziende.setSelectedIndex(1);
        assertEquals("azienda2", form.getSelectedAzienda());
        form.listaAziende.setSelectedIndex(2);
        assertEquals("azienda3", form.getSelectedAzienda());
    }

    @Test
    public void testGetHiddenSelectedMeseInt() {
        String prova = "Maggio";
        form.mese.setText(prova);
        assertEquals(5, form.getHiddenSelectedMeseInt());
    }

    @Test
    public void testGetHiddenSelectedMeseString() {
        String prova = "Maggio";
        form.mese.setText(prova);
        assertEquals(prova,form.getHiddenSelectedMeseString());
    }

    @Test
    public void testRemoveSelectedAzienda() throws NoSelectedRow {
        assertEquals(3,form.listaAziende.getModel().getSize());
        form.listaAziende.setSelectedIndex(6);
        try{
            form.removeSelectedAzienda();
            fail();
        } catch (NoSelectedRow e){}
        assertEquals(3,form.listaAziende.getModel().getSize());
        form.listaAziende.setSelectedIndex(1);
        form.removeSelectedAzienda();
        assertEquals(2,form.listaAziende.getModel().getSize());
        assertEquals("azienda1",form.listaAziende.getModel().getElementAt(0));
        assertEquals("azienda3",form.listaAziende.getModel().getElementAt(1));
        form.listaAziende.setSelectedIndex(0);
        form.removeSelectedAzienda();
        assertEquals(1,form.listaAziende.getModel().getSize());
        assertEquals("azienda3",form.listaAziende.getModel().getElementAt(0));
        form.listaAziende.setSelectedIndex(0);
        form.removeSelectedAzienda();
        assertEquals(0,form.listaAziende.getModel().getSize());
    }
}
