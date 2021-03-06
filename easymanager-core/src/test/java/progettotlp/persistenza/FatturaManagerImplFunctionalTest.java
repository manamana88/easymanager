package progettotlp.persistenza;

import java.util.List;

import org.junit.Before;
import progettotlp.facilities.DateUtils;
import progettotlp.interfaces.AziendaInterface;
import progettotlp.interfaces.BeneInterface;
import progettotlp.interfaces.DdTInterface;
import progettotlp.interfaces.FatturaInterface;
import progettotlp.classes.Bene;

import java.io.File;
import java.math.BigDecimal;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;

import org.junit.Test;

import progettotlp.classes.Azienda;
import progettotlp.classes.DdT;
import progettotlp.classes.Fattura;
import progettotlp.exceptions.PersistenzaException;
import progettotlp.facilities.ConfigurationManager;
import static org.junit.Assert.*;

/**
 *
 * @author vincenzo
 */
public class FatturaManagerImplFunctionalTest extends AbstractTest{
    protected FatturaManagerImpl fatturaManager;

    @Before
    public void setup(){
        if (fatturaManager!=null){
            fatturaManager.close();
        }
        fatturaManager=new FatturaManagerImpl(properties);
    }

    @Test
    public void testRegistraFattura() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareFatturaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        DdTInterface ddt2=retrieveObject(DdT.class, 2L,fatturaManager);
        FatturaInterface newFattura = new Fattura(
                Arrays.asList(ddt2),
                new Date(),
                new Date(),
                1,
                ddt2.getCliente(),
                new BigDecimal("10"),
                ConfigurationManager.getIvaDefault(),
                new BigDecimal("2.1"),
                new BigDecimal("12.1"),
                null);
        ddt2.setFattura(newFattura);
        try{
            fatturaManager.registraFattura(newFattura);
            fail();
        } catch (PersistenzaException e){}
        newFattura.setId(4);
        fatturaManager.registraFattura(newFattura);
        assertNotNull(retrieveObject(Fattura.class, 2L,fatturaManager));
    }

    @Test
    public void testModificaFattura() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareFatturaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        FatturaInterface retrieved = retrieveObject(Fattura.class, 1L,fatturaManager);
        assertNotNull(retrieved);
        BigDecimal newNetto = new BigDecimal("154");
        Integer newId = 15;
        retrieved.setNetto(newNetto);
        retrieved.setId(newId);
        fatturaManager.modificaFattura(retrieved);
        retrieved = retrieveObject(Fattura.class, 1L,fatturaManager);
        assertTrue(newNetto.compareTo(retrieved.getNetto()) == 0);
        assertEquals(newId, retrieved.getId());
    }

    @Test
    public void testCancellaFatturaById() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareFatturaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        fatturaManager.cancellaFattura(1);
        assertNull(retrieveObject(Fattura.class, 1L,fatturaManager));
        assertNotNull(retrieveObject(DdT.class, 3L,fatturaManager));
    }

    @Test
    public void testGetAllFatture() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareFatturaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        assertEquals(2, fatturaManager.getAllFatture(false,false).size());
    }

    @Test
    public void testGetFattureByAziendaName() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareFatturaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        assertEquals(1, fatturaManager.getFattureByAziendaName("CDTaglio").size());
        assertEquals(1, fatturaManager.getFattureByAziendaName("ABTaglio").size());
    }

    @Test
    public void testGetFattura() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareFatturaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        assertNotNull(fatturaManager.getFattura(1,false,false));
        assertNull(fatturaManager.getFattura(4,false,false));
    }

    @Test
    public void testGetLastFattura() throws Exception {
        //assertEquals(0, fatturaManager.getLastFattura());
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareFatturaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);

        fatturaManager.close();
        fatturaManager = new FatturaManagerImpl(properties);
        assertEquals(2, fatturaManager.getLastFattura());
    }

    @Test
    public void testExistsFattura_int_Azienda() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareFatturaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        AziendaInterface azienda3 = retrieveObject(Azienda.class, 3L,fatturaManager);
        AziendaInterface azienda2 = retrieveObject(Azienda.class, 2L,fatturaManager);
        assertFalse(fatturaManager.existsFattura(6, azienda2));
        assertFalse(fatturaManager.existsFattura(5, azienda2));
        assertFalse(fatturaManager.existsFattura(7, azienda2));
        assertTrue(fatturaManager.existsFattura(6, azienda3));
        assertFalse(fatturaManager.existsFattura(5, azienda3));
        assertFalse(fatturaManager.existsFattura(7, azienda3));
    }

    @Test
    public void testExistsFatturaById_int() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareFatturaTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        assertTrue(fatturaManager.existsFattura(1));
        assertFalse(fatturaManager.existsFattura(4));
    }

    @Test
    public void testGetLastSameBene() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareLastBeneTest.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        BeneInterface b=new Bene();
        b.setCodice("0001");
        b.setCampionario(Boolean.TRUE);
        b.setPiazzato(Boolean.TRUE);
        b.setPrimoCapo(Boolean.TRUE);
        b.setPrototipo(Boolean.TRUE);
        LastSameBeneFatturatoInfos lastSameBene = fatturaManager.getLastSameBeneFatturatoInfos(b);
        assertNotNull(lastSameBene);
        assertEquals(new Integer(4),lastSameBene.getFatturaId());
        Date emissione = DateUtils.parseDate("30/03/2012");
        assertEquals(emissione,lastSameBene.getFatturaEmissione());
        BeneInterface b1 = lastSameBene.getBene();
        assertEquals("0001",b1.getCodice());
        assertEquals("C0001",b1.getCommessa());
        assertEquals("Abito",b1.getDescrizione());
        assertTrue(new BigDecimal("15").compareTo(b1.getQta()) == 0);
        assertTrue(new BigDecimal("3").compareTo(b1.getPrezzo()) == 0);
        assertTrue(new BigDecimal("45").compareTo(b1.getTot()) == 0);
        assertTrue(new BigDecimal("3").compareTo(b1.getPrezzo()) == 0);
        assertTrue(b1.getCampionario());
        assertTrue(b1.getPiazzato());
        assertTrue(b1.getPrimoCapo());
        assertTrue(b1.getPrototipo());

        b.setPrimoCapo(Boolean.FALSE);
        assertNull(fatturaManager.getLastSameBeneFatturatoInfos(b));
    }

}
