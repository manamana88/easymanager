/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package progettotlp.persistenza;

import java.util.ArrayList;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import org.junit.Test;
import progettotlp.classes.Azienda;
import progettotlp.exceptions.PersistenzaException;
import static org.junit.Assert.*;

/**
 *
 * @author vincenzo
 */
public class AziendaManagerImplFunctionalTest extends AbstractTest{
    protected AziendaManagerImpl aziendaManager;

    @Test
    public void testRegistraAzienda() throws PersistenzaException{
        Azienda a = new Azienda();
        a.setCap("65129");
        a.setCitta("Pescara");
        a.setCivico("1");
        a.setCodFis("BRRVCN88M20G482K");
        a.setFax("0854322029");
        a.setMail("vinci.88@tisclai.it");
        a.setNazione("Italia");
        a.setNome("CRTaglio");
        a.setPIva("01234567890");
        a.setPrincipale(false);
        a.setProvincia("PE");
        a.setTelefono("0854322029");
        a.setVia("via A.Volta");
        a.setTassabile(false);

        aziendaManager.registraAzienda(a);

        Long id = a.getId();
        assertTrue(id != null && id > -1);
        Azienda retrieved = retrieveObject(Azienda.class, id,aziendaManager);
        assertNotNull(retrieved);
        assertEquals(retrieved.getCap(), "65129");
        assertEquals("Pescara", retrieved.getCitta());
        assertEquals("1", retrieved.getCivico());
        assertEquals("BRRVCN88M20G482K", retrieved.getCodFis());
        assertEquals("0854322029", retrieved.getFax());
        assertEquals("vinci.88@tisclai.it", retrieved.getMail());
        assertEquals("Italia", retrieved.getNazione());
        assertEquals("CRTaglio", retrieved.getNome());
        assertEquals("01234567890", retrieved.getPIva());
        assertFalse(retrieved.isPrincipale());
        assertEquals("PE", retrieved.getProvincia());
        assertEquals("0854322029", retrieved.getTelefono());
        assertEquals("via A.Volta", retrieved.getVia());
        assertFalse(retrieved.isTassabile());
    }

    @Test
    public void testModificaAzienda() throws IOException, SQLException, PersistenzaException {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareAziendaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        Azienda toModify = retrieveObject(Azienda.class, 1L,aziendaManager);
        assertNotNull(toModify);
        toModify.setPIva("00000111112");
        aziendaManager.modificaAzienda(toModify);
        Azienda modified = retrieveObject(Azienda.class, 1L,aziendaManager);
        assertNotNull(modified);
        assertEquals("00000111112", modified.getPIva());
    }

    @Test
    public void testCancellaAzienda() throws IOException, SQLException, PersistenzaException {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareAziendaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        Azienda toDelete = retrieveObject(Azienda.class, 1L,aziendaManager);
        assertNotNull(toDelete);
        aziendaManager.cancellaAzienda(toDelete);
        Azienda deleted = retrieveObject(Azienda.class, 1L,aziendaManager);
        assertNull(deleted);
    }

    @Test
    public void testNumAziende() throws IOException, SQLException {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareAziendaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        assertEquals(3, aziendaManager.getNumAziende());
    }

    @Test
    public void testGetAziende() throws IOException, SQLException {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareAziendaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        List<Azienda> aziende = aziendaManager.getAziende();
        assertEquals(3, aziende.size());
        assertEquals(new Long(2), aziende.get(0).getId());
        assertEquals(new Long(3), aziende.get(1).getId());
        assertEquals(new Long(1), aziende.get(2).getId());
    }

    @Test
    public void testGetAziendaPerNome() throws IOException, SQLException {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareAziendaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        Azienda aziendaPerNome = aziendaManager.getAziendaPerNome("ABTaglio");
        assertNotNull(aziendaPerNome);
        assertEquals("ABTaglio", aziendaPerNome.getNome());
        aziendaPerNome = aziendaManager.getAziendaPerNome("ABBTaglio");
        assertNull(aziendaPerNome);
    }

    @Test
    public void testGetAziendePerNome() throws SQLException, IOException{
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareAziendaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        List<String> nomi = new ArrayList<String>();
        nomi.add("ABTaglio");
        nomi.add("CDTaglio");
        List<Azienda> aziendePerNome = aziendaManager.getAziendePerNome(nomi);
        assertEquals(2, aziendePerNome.size());
        nomi.add("FAKE_NAME");
        aziendePerNome=aziendaManager.getAziendePerNome(nomi);
        assertEquals(2, aziendePerNome.size());
    }

    @Test
    public void testGetAziendaPrincipale() throws IOException, SQLException {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareAziendaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        Azienda aziendaPrincipale = aziendaManager.getAziendaPrincipale();
        assertNotNull(aziendaPrincipale);
        assertEquals("CRTaglio", aziendaPrincipale.getNome());
    }

    @Test
    public void testGetAziendeNonPrincipali() throws IOException, SQLException {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareAziendaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        List<Azienda> aziende = aziendaManager.getAziendeNonPrincipali();
        assertEquals(2, aziende.size());
        assertEquals("ABTaglio", aziende.get(0).getNome());
        assertEquals("CDTaglio", aziende.get(1).getNome());
    }

    @Test
    public void testIsAziendaTassabileByName() throws SQLException, IOException {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareAziendaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        assertTrue(aziendaManager.isAziendaTassabileByName("CDTaglio"));
        assertFalse(aziendaManager.isAziendaTassabileByName("ABTaglio"));
    }

    @Override
    protected List<Class<? extends AbstractPersistenza>> getManagersClass() {
        List<Class<? extends AbstractPersistenza>> res = new ArrayList<Class<? extends AbstractPersistenza>>();
        res.add(AziendaManagerImpl.class);
        return res;
    }

    @Override
    protected Object getObjectToInitialize() {
        return this;
    }

}
