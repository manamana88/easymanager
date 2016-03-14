/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.ui;

import java.io.File;
import java.net.URL;
import progettotlp.persistenza.AbstractPersistenza;
import progettotlp.persistenza.AbstractTest;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JDialog;
import org.junit.Before;
import org.junit.Test;
import progettotlp.persistenza.AziendaManager;
import progettotlp.persistenza.AziendaManagerImpl;
import progettotlp.classes.Azienda;
import progettotlp.exceptions.toprint.GenericExceptionToPrint;
import progettotlp.exceptions.toprint.ValidationException;
import static org.junit.Assert.*;


/**
 *
 * @author vincenzo
 */
public class FormAziendaUtilitiesFunctionalTest extends AbstractTest{

    protected FormAziendaUtilities form;

    public FormAziendaUtilitiesFunctionalTest() {
         form= new FormAziendaUtilities(new JDialog(), null);
    }

    @Before
    public void init() throws Exception{
        List<Class<?extends Object>> ignore = new ArrayList<Class<? extends Object>>();
        ignore.add(AziendaManager.class);
        UiTestUtilities.initializeClass(form, ignore);

        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareFormSceltaAziendaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
    }
    
    @Test
    public void testRegistraAzienda() throws ValidationException, GenericExceptionToPrint {
        Boolean aziendaPrincipale=false;
        Boolean giuridico=false;
        String cod_fis = "BRRVCN88M20G482J";
        String email = "vinci.87@tiscali.it";
        String iva = "01234567899";
        String nomeAzienda = "azienda1";

        form.aziendaPrincipale.setText(aziendaPrincipale.toString());
        form.cod_fis.setText(cod_fis);
        form.email.setText(email);
        form.giuridico.setSelected(giuridico);
        form.iva.setText(iva);
        form.nomeAzienda.setText(nomeAzienda);

        form.registraAzienda();

        Azienda retrieved = retrieveObject(Azienda.class, 5L,(AziendaManagerImpl)form.aziendaManager);
        assertNotNull(retrieved);
        assertEquals(aziendaPrincipale, retrieved.isPrincipale());
        assertEquals(cod_fis, retrieved.getCodFis());
        assertEquals(email, retrieved.getMail());
        assertEquals(iva, retrieved.getPIva());
        assertEquals(nomeAzienda, retrieved.getNome());
    }

    @Test
    public void testModificaAzienda() throws GenericExceptionToPrint, ValidationException {
        Boolean aziendaPrincipale=false;
        Boolean giuridico=true;
        String cod_fis = "BRRVCN88M20G482J";
        String email = "vinci.88modificato@tiscali.it";
        String iva = "00000111111";
        String nomeAzienda = "azienda10";
        Long id=2L;

        form.aziendaPrincipale.setText(aziendaPrincipale.toString());
        form.cod_fis.setText(cod_fis);
        form.email.setText(email);
        form.giuridico.setSelected(giuridico);
        form.iva.setText(iva);
        form.nomeAzienda.setText(nomeAzienda);
        form.aziendaId.setText(id.toString());
        
        form.modificaAzienda();

        Azienda retrieved = retrieveObject(Azienda.class, 2L,(AziendaManagerImpl)form.aziendaManager);
        assertNotNull(retrieved);
        assertEquals(aziendaPrincipale, retrieved.isPrincipale());
        assertEquals(cod_fis, retrieved.getCodFis());
        assertEquals(email, retrieved.getMail());
        assertEquals(iva, retrieved.getPIva());
        assertEquals(nomeAzienda, retrieved.getNome());
    }

    @Override
    protected List<Class<? extends AbstractPersistenza>> getManagersClass() {
        List<Class<? extends AbstractPersistenza>> res = new ArrayList<Class<? extends AbstractPersistenza>>();
        res.add(AziendaManagerImpl.class);
        return res;
    }

    @Override
    protected Object getObjectToInitialize() {
        return form;
    }

}
