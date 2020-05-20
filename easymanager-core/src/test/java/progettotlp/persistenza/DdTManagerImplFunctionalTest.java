package progettotlp.persistenza;

import java.util.*;
import java.io.File;
import java.math.BigDecimal;
import java.net.URL;

import org.junit.Test;
import progettotlp.classes.Azienda;
import progettotlp.classes.Bene;
import progettotlp.classes.DdT;
import progettotlp.facilities.DateUtils;
import progettotlp.interfaces.AziendaInterface;
import progettotlp.interfaces.BeneInterface;
import progettotlp.interfaces.DdTInterface;

import static org.junit.Assert.*;

/**
 *
 * @author vincenzo
 */
public class DdTManagerImplFunctionalTest extends AbstractTest{
    protected DdTManagerImpl ddTManager;

    @Test
    public void testRegistraDdT() throws Exception{
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareDdTTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        BeneInterface b1 = new Bene();
        b1.setCodice("cod");
        b1.setCommessa("com");
        b1.setDescrizione("descr");
        b1.setQta(new BigDecimal("1"));
        BeneInterface b2 = new Bene();
        b2.setCodice("cod2");
        b2.setCommessa("com2");
        b2.setDescrizione("descr2");
        b2.setQta(new BigDecimal("2"));
        DdTInterface toSave = new DdT();
        toSave.setBeni(Arrays.asList(b1, b2));
        Date data = DateUtils.parseDate("01-01-2012");
        toSave.setData(data);
        toSave.setId(1);
        Azienda cliente = retrieveObject(Azienda.class, 2L,ddTManager);
        toSave.setCliente(cliente);
        toSave.setMezzo("Cessionario");
        toSave.setCausale("Reso c/adesivazione");
        ddTManager.registraDdT(toSave);


        DdTInterface retrieved = retrieveObject(DdT.class, toSave.getRealId(),ddTManager, Collections.singletonList("beni"));
        assertNotNull(retrieved);
        List<BeneInterface> beni = retrieved.getBeni();
        assertEquals(2, beni.size());
        b1 = beni.get(0);
        assertEquals("cod", b1.getCodice());
        assertEquals("com", b1.getCommessa());
        assertEquals("descr", b1.getDescrizione());
        assertTrue(new BigDecimal("1").compareTo(b1.getQta()) == 0);
        b2 = beni.get(1);
        assertEquals("cod2", b2.getCodice());
        assertEquals("com2", b2.getCommessa());
        assertEquals("descr2", b2.getDescrizione());
        assertTrue(new BigDecimal("2").compareTo(b2.getQta()) == 0);
        assertEquals(data, retrieved.getData());
        assertEquals(cliente, retrieved.getCliente());
        assertEquals("Cessionario", retrieved.getMezzo());
        assertEquals("Reso c/adesivazione", retrieved.getCausale());
    }

    @Test
    public void testModificaDdT() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareDdTTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        DdTInterface toModify = retrieveObject(DdT.class, 1L,ddTManager, Collections.singletonList("beni"));
        toModify.setId(234);

        List<BeneInterface> beni = toModify.getBeni();
        for (int i=0; i<beni.size();i++){
            BeneInterface b=beni.get(i);
            Long id = b.getId();
            if (id.equals(1L)) {
                b.setQta(new BigDecimal("1"));
                b.setCampionario(Boolean.TRUE);
                b.setPiazzato(Boolean.TRUE);
                b.setPrimoCapo(Boolean.TRUE);
                b.setPrototipo(Boolean.TRUE);
            } else if (id.equals(2L)){
                beni.remove(b);
            } else {
                fail();
            }
        }
        Bene beneToAdd = new Bene("cod3", "com3", "descr3", new BigDecimal("30"), true, true, true, true,true);
        beni.add(beneToAdd);

        ddTManager.modificaDdT(toModify);
        DdTInterface modified = retrieveObject(DdT.class, 1L,ddTManager, Collections.singletonList("beni"));
        assertEquals(new Integer(234), modified.getId());
        beni = modified.getBeni();
        assertEquals(2, beni.size());
        for (BeneInterface b : beni) {
            if (b.getId().equals(1L)) {
                assertTrue(new BigDecimal("1").compareTo(b.getQta()) == 0);
                assertTrue(new BigDecimal("21").compareTo(b.getPrezzo()) == 0);
                assertTrue(b.getCampionario());
                assertTrue(b.getPiazzato());
                assertTrue(b.getPrimoCapo());
                assertTrue(b.getPrototipo());
                assertTrue(b.getInteramenteAdesivato());
            } else if (b.getId().equals(3L)) {
                assertEquals(beneToAdd, b);
            } else {
                fail("This bene with id " + b.getId() + " had to be deleted");
            }
        }
    }

    @Test
    public void testCancellaDdT() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareDdTTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        DdTInterface toDelete = retrieveObject(DdT.class, 1L,ddTManager);
        assertNotNull(toDelete);
        ddTManager.cancellaDdT(toDelete.getRealId());
        DdTInterface deleted = retrieveObject(DdT.class, 1L,ddTManager);
        assertNull(deleted);

    }

    @Test
    public void testGetLastDdT() throws Exception {
        assertEquals(0, ddTManager.getLastDdT());
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareDdTTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        ddTManager.close();
        ddTManager=new DdTManagerImpl(properties);
        assertEquals(108, ddTManager.getLastDdT());
    }

    @Test
    public void testGetAllDdT_0args() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareDdTTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        assertEquals(3, ddTManager.getAllDdT(false,false).size());
    }

    @Test
    public void testGetAllDdT_Azienda_int() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareDdTTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        AziendaInterface azienda = retrieveObject(Azienda.class, 3L,ddTManager);
        List<DdTInterface> allDdT = ddTManager.getAllDdT(azienda, 6,false,false);
        assertEquals(2, allDdT.size());
        allDdT = ddTManager.getAllDdT(azienda, 5,false,false);
        assertTrue(allDdT.isEmpty());
        allDdT = ddTManager.getAllDdT(azienda, 7,false,false);
        assertTrue(allDdT.isEmpty());
        azienda = retrieveObject(Azienda.class, 1L,ddTManager);
        allDdT = ddTManager.getAllDdT(azienda, 6,false,false);
        assertTrue(allDdT.isEmpty());
    }

    @Test
    public void testGetAllDdTWithoutFattura() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareDdTTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        assertEquals(2, ddTManager.getAllDdTWithoutFattura(false,false).size());
    }

    @Test
    public void testGetDdT() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareDdTTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        assertNotNull(ddTManager.getDdT(1L,false,false));
        assertNull(ddTManager.getDdT(4L,false,false));
    }

    @Test
    public void testGetDdTById() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareDdTTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        assertNotNull(ddTManager.getDdTById(106,false,false));
        assertNull(ddTManager.getDdTById(110,false,false));
    }

    @Test
    public void testGetBeniDdT() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareDdTTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        List<Bene> beniDdT = ddTManager.getBeniDdT(106);
        assertNotNull(beniDdT);
        assertEquals(2, beniDdT.size());
        assertTrue(ddTManager.getBeniDdT(120).isEmpty());
    }

    @Test
    public void testIsEmptyDdTListMese() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareDdTTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        AziendaInterface azienda1 = retrieveObject(Azienda.class, 1L,ddTManager);
        AziendaInterface azienda2 = retrieveObject(Azienda.class, 2L,ddTManager);
        AziendaInterface azienda3 = retrieveObject(Azienda.class, 3L,ddTManager);
        assertTrue(ddTManager.isEmptyDdTListMese(6, azienda1));
        assertFalse(ddTManager.isEmptyDdTListMese(6, azienda2));
        assertFalse(ddTManager.isEmptyDdTListMese(6, azienda3));
        assertTrue(ddTManager.isEmptyDdTListMese(5, azienda2));
        assertTrue(ddTManager.isEmptyDdTListMese(5, azienda3));
        assertTrue(ddTManager.isEmptyDdTListMese(7, azienda2));
        assertTrue(ddTManager.isEmptyDdTListMese(7, azienda3));
    }

    @Test
    public void testExistsDdT() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareDdTTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        assertTrue(ddTManager.existsDdT(1L));
        assertTrue(ddTManager.existsDdT(2L));
        assertTrue(ddTManager.existsDdT(3L));
        assertFalse(ddTManager.existsDdT(4L));
    }

    @Test
    public void testExistsDdTById() throws Exception {
        URL systemResource = ClassLoader.getSystemResource("progettotlp/db/scripts/prepareDdTTests.sql");
        File file = new File(systemResource.getFile());
        executeSQL(file);
        assertTrue(ddTManager.existsDdTById(106));
        assertTrue(ddTManager.existsDdTById(107));
        assertTrue(ddTManager.existsDdTById(108));
        assertFalse(ddTManager.existsDdTById(110));
    }

    @Override
    protected List<Class<? extends AbstractPersistenza>> getManagersClass() {
        List<Class<? extends AbstractPersistenza>> res = new ArrayList<>();
        res.add(DdTManagerImpl.class);
        return res;
    }

    @Override
    protected Object getObjectToInitialize() {
        return this;
    }

}